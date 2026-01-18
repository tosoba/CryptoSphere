package com.trm.cryptosphere.ui.token.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.core.util.resolve
import kotlin.math.sign

@Composable
fun TokenFeedParameterCard(
  parameter: TokenFeedParameter,
  shape: Shape,
  modifier: Modifier = Modifier,
) {
  Card(modifier = modifier, shape = shape) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
      Text(
        text = parameter.label.resolve(),
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )

      val valueStyle = MaterialTheme.typography.titleLarge
      val valueChangeStyle = MaterialTheme.typography.labelLarge
      val valueChange = parameter.valueChange
      val valueChangeFormat = parameter.valueChangeFormat

      val valueChangeText =
        if (valueChange != null && valueChangeFormat != null) valueChangeFormat(valueChange)
        else null
      val valueChangeId = "valueChange"

      val textMeasurer = rememberTextMeasurer()
      val textSize =
        if (valueChangeText != null) {
          remember(valueChangeText, valueChangeStyle) {
            textMeasurer.measure(valueChangeText, valueChangeStyle).size
          }
        } else {
          null
        }

      Text(
        text =
          buildAnnotatedString {
            append(parameter.valueFormat(parameter.value))
            if (valueChangeText != null) {
              append(' ')
              appendInlineContent(valueChangeId)
            }
          },
        inlineContent =
          if (textSize != null) {
            mapOf(
              valueChangeId to
                InlineTextContent(
                  Placeholder(
                    width = with(LocalDensity.current) { (textSize.width.toDp() + 8.dp).toSp() },
                    height = with(LocalDensity.current) { textSize.height.toDp().toSp() },
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                  )
                ) {
                  val valueChangePositive = valueChange?.toDouble()?.sign == 1.0
                  Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                      Modifier.fillMaxSize()
                        .background(
                          color = if (valueChangePositive) Color.Green else Color.Red,
                          shape = RoundedCornerShape(4.dp),
                        ),
                  ) {
                    Text(
                      text = valueChangeText.orEmpty(),
                      style =
                        if (valueChangePositive) valueChangeStyle
                        else valueChangeStyle.copy(color = Color.White),
                      modifier = Modifier.padding(horizontal = 4.dp),
                    )
                  }
                }
            )
          } else {
            emptyMap()
          },
        style = valueStyle,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )
    }
  }
}
