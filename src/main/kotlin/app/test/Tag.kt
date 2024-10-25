package app.test

import androidx.compose.ui.*
import androidx.compose.ui.platform.*

fun Modifier.addTestTag(tag: String): Modifier = testTag(tag)