package com.trm.cryptosphere.core.cache.disk

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import io.ktor.util.flattenEntries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.BufferedSink
import okio.BufferedSource
import okio.ByteString.Companion.encodeUtf8
import okio.FileSystem
import okio.Path

/**
 * storage that uses file system to store cache data.
 *
 * @param fileSystem underlying filesystem.
 * @param directory directory to store cache data.
 * @param maxSize maximum cache size
 * @param dispatcher dispatcher to use for file operations.
 */
internal class DiskCacheStorage(
  private val fileSystem: FileSystem,
  directory: Path,
  maxSize: Long,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CacheStorage {
  private val diskLruCache by lazy { DiskLruCache(fileSystem, directory, dispatcher, maxSize) }

  override suspend fun store(url: Url, data: CachedResponseData) {
    diskLruCache.edit(url.hash())?.let { editor ->
      try {
        fileSystem.write(editor.file()) { writeCache(this, data) }
        editor.commit()
      } catch (_: Exception) {
        editor.abortQuietly()
      }
    }
  }

  override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
    return diskLruCache.get(url.hash())?.use {
      try {
        fileSystem.read(it.file()) { readCache(this) }
      } catch (_: Exception) {
        null
      }
    }
  }

  override suspend fun findAll(url: Url): Set<CachedResponseData> {
    return find(url, emptyMap())?.let(::setOf) ?: emptySet()
  }

  override suspend fun remove(url: Url, varyKeys: Map<String, String>) {
    removeAll(url)
  }

  override suspend fun removeAll(url: Url) {
    diskLruCache.remove(url.hash())
  }

  private fun readCache(source: BufferedSource): CachedResponseData {
    val url = source.readUtf8Line()!!
    val status = HttpStatusCode(source.readInt(), source.readUtf8Line()!!)
    val version = HttpProtocolVersion.parse(source.readUtf8Line()!!)
    val headersCount = source.readInt()
    val headers = HeadersBuilder()
    repeat(headersCount) {
      val key = source.readUtf8Line()!!
      val value = source.readUtf8Line()!!
      headers.append(key, value)
    }
    val requestTime = GMTDate(source.readLong())
    val responseTime = GMTDate(source.readLong())
    val expirationTime = GMTDate(source.readLong())
    val varyKeysCount = source.readInt()
    val varyKeys = buildMap {
      repeat(varyKeysCount) {
        val key = source.readUtf8Line()!!
        val value = source.readUtf8Line()!!
        put(key, value)
      }
    }
    val bodyCount = source.readInt()
    val body = ByteArray(bodyCount)
    source.readFully(body)
    return CachedResponseData(
      url = Url(url),
      statusCode = status,
      requestTime = requestTime,
      responseTime = responseTime,
      version = version,
      expires = expirationTime,
      headers = headers.build(),
      varyKeys = varyKeys,
      body = body,
    )
  }

  private fun writeCache(channel: BufferedSink, cache: CachedResponseData) {
    channel.writeUtf8(cache.url.toString() + "\n")
    channel.writeInt(cache.statusCode.value)
    channel.writeUtf8(cache.statusCode.description + "\n")
    channel.writeUtf8(cache.version.toString() + "\n")
    val headers = cache.headers.flattenEntries()
    channel.writeInt(headers.size)
    for ((key, value) in headers) {
      channel.writeUtf8(key + "\n")
      channel.writeUtf8(value + "\n")
    }
    channel.writeLong(cache.requestTime.timestamp)
    channel.writeLong(cache.responseTime.timestamp)
    channel.writeLong(cache.expires.timestamp)
    channel.writeInt(cache.varyKeys.size)
    for ((key, value) in cache.varyKeys) {
      channel.writeUtf8(key + "\n")
      channel.writeUtf8(value + "\n")
    }
    channel.writeInt(cache.body.size)
    channel.write(cache.body)
  }

  companion object {
    const val DEFAULT_MAX_SIZE = 1024L * 1024L * 10L
  }
}

private fun Url.hash() = toString().encodeUtf8().md5().hex()

private fun DiskLruCache.Editor.abortQuietly() {
  try {
    abort()
  } catch (_: Exception) {}
}
