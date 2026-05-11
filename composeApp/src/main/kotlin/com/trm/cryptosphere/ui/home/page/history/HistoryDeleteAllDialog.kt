package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR

@Composable
fun HistoryDeleteAllDialog(onDismissRequest: () -> Unit, onConfirm: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    confirmButton = {
      TextButton(
        onClick = {
          onConfirm()
          onDismissRequest()
        }
      ) {
        Text(MR.strings.confirm.resolve())
      }
    },
    dismissButton = {
      TextButton(onClick = onDismissRequest) { Text(MR.strings.cancel.resolve()) }
    },
    title = { Text(MR.strings.delete_history.resolve()) },
    text = { Text(MR.strings.delete_history_message.resolve()) },
  )
}
