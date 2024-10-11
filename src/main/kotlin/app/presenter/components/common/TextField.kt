package app.presenter.components.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.util.regex.*

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    startValue: String,
    label: String,
    onValueChange: (String) -> Unit,
    showEditIcon: Boolean = true,
    multiLine: Boolean = false,
    maxLength: Int = 72,
    textStyle: TextStyle,
    labelTextStyle: TextStyle,
    textColor: Color
) {
    var text by remember(startValue) {
        mutableStateOf(startValue)
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = labelTextStyle,
            color = textColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6f))
                .border(1.dp, textColor, RoundedCornerShape(6f))
                .padding(4.dp)
        ) {
            if (showEditIcon) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(10.dp),
                    imageVector = Icons.Default.ModeEdit,
                    contentDescription = "Edit icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { newValue ->
                    if (newValue.length <= maxLength) {
                        text = newValue
                        onValueChange(text)
                    }
                },
                singleLine = !multiLine,
                textStyle = textStyle.copy(color = textColor),
                cursorBrush = SolidColor(textColor)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OptionsTextField(
    modifier: Modifier = Modifier,
    startValue: String,
    label: String,
    showEditIcon: Boolean = true,
    decorated: Boolean = false,
    options: List<String> = emptyList(),
    onOptionSelected: (Int) -> Unit,
    isError: Boolean = false
) {
    var text by remember(startValue) {
        mutableStateOf(startValue)
    }

    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = options, block = {
        if (!options.contains(text)) text = options.firstOrNull().toString()
    })

    if (options.isEmpty()) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (showEditIcon) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.ModeEdit,
                    contentDescription = "Edit icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = "No options",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = isMenuExpanded,
            onExpandedChange = { newValue ->
                isMenuExpanded = newValue
            }
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (showEditIcon) {
                    androidx.compose.material3.Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Edit icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    modifier = if (decorated) {
                        Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(5))
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5))
                            .padding(4.dp)
                    } else {
                        Modifier
                    },
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (!isError) {
                            if (decorated) {
                                MaterialTheme.colorScheme.onBackground
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        textAlign = TextAlign.End
                    )
                )
            }
            ExposedDropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .border(2.dp, MaterialTheme.colorScheme.primary),
                expanded = isMenuExpanded,
                onDismissRequest = {
                    isMenuExpanded = false
                }
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        onClick = {
                            text = option
                            onOptionSelected(index)
                            isMenuExpanded = false
                        }
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeTextField(
    modifier: Modifier = Modifier,
    startValue: String,
    label: String,
    onValueChange: (String) -> Unit,
    showEditIcon: Boolean = true,
    isError: Boolean = false,
    onErrorChange: (Boolean) -> Unit
) {
    var text by remember {
        mutableStateOf(startValue)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(4.dp))
        if (showEditIcon) {
            androidx.compose.material3.Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.ModeEdit,
                contentDescription = "Edit icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                if (newValue.length <= 72) {
                    onErrorChange(!timeWithoutSecondsPattern.matches(newValue))

                    text = newValue
                    onValueChange(text)
                }
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (!isError) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.error
                },
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
        )
    }
}