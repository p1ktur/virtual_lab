package app.presenter.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.auth.*
import app.domain.viewModels.educationalMaterial.*
import app.presenter.components.common.*
import app.presenter.theme.*
import java.awt.*
import java.net.*

@Composable
fun EducationalMaterialScreen(
    uiState: EducationalMaterialUiState,
    onUiAction: (EducationalMaterialUiAction) -> Unit
) {
    val canEdit = remember(uiState.authType) {
        uiState.authType is AuthType.Teacher || uiState.authType is AuthType.Administrator
    }
    val educationalMaterialIsCreated = remember(uiState.educationalMaterial) { uiState.educationalMaterial.id > 0 }

    val desktop = Desktop.getDesktop()
    val isDesktopEnabled = Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.screenTwo)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (canEdit && educationalMaterialIsCreated) {
                    app.presenter.components.buttons.IconButton(
                        modifier = Modifier.size(24.dp),
                        icon = Icons.Filled.Delete,
                        actionText = "Delete Educational Material",
                        color = LocalAppTheme.current.text,
                        backgroundColor = Color.Transparent,
                        shape = RectangleShape,
                        onClick = {
                            onUiAction(EducationalMaterialUiAction.DeleteEducationalMaterial)
                        }
                    )
                }
            }
            if (canEdit) {
                SingleLineTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = uiState.educationalMaterial.name,
                    label = "Name:",
                    onValueChange = { newValue ->
                        onUiAction(EducationalMaterialUiAction.UpdateName(newValue))
                    },
                    maxLength = 48,
                    showEditIcon = false,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    labelTextStyle = MaterialTheme.typography.labelLarge,
                    textColor = LocalAppTheme.current.text
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Name: ${uiState.educationalMaterial.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (canEdit) {
                MultiLineTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = uiState.educationalMaterial.description,
                    label = "Description:",
                    placeholder = "Type description:",
                    onValueChange = { newValue ->
                        onUiAction(EducationalMaterialUiAction.UpdateDescription(newValue))
                    },
                    maxLength = 1920,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    labelTextStyle = MaterialTheme.typography.labelLarge,
                    placeholderTextStyle = MaterialTheme.typography.labelLarge,
                    textColor = LocalAppTheme.current.text
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Description: ${uiState.educationalMaterial.description}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = LocalAppTheme.current.text,
                fillMaxWidth = 1f
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = "Links",
                style = MaterialTheme.typography.bodyLarge,
                color = LocalAppTheme.current.text
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (uiState.educationalMaterial.urls.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                            text = "No Links yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalAppTheme.current.text
                        )
                    }
                }
                itemsIndexed(uiState.educationalMaterial.urls) { index, url ->
                    var isEditing by remember { mutableStateOf(url.isBlank()) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            SingleLineTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp)
                                    .padding(horizontal = 4.dp),
                                text = url,
                                label = "Link:",
                                onValueChange = { newValue ->
                                    onUiAction(EducationalMaterialUiAction.UpdateURL(index, newValue))
                                },
                                maxLength = 200,
                                showEditIcon = false,
                                showFrame = false,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                labelTextStyle = MaterialTheme.typography.bodyMedium,
                                textColor = LocalAppTheme.current.text
                            )
                        } else {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp)
                                    .clickable {
                                        if (url.startsWith("http://") || url.startsWith("https://")) {
                                            if (isDesktopEnabled) desktop.browse(URI.create(url))
                                        }
                                    }
                                    .padding(horizontal = 4.dp),
                                text = url,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.highlightColor,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                        if (canEdit && educationalMaterialIsCreated) {
                            app.presenter.components.buttons.IconButton(
                                modifier = Modifier.size(28.dp),
                                icon = Icons.Filled.Edit,
                                actionText = "Edit Link",
                                color = LocalAppTheme.current.text,
                                backgroundColor = Color.Transparent,
                                shape = RectangleShape,
                                onClick = {
                                    isEditing = !isEditing
                                }
                            )
                            app.presenter.components.buttons.IconButton(
                                modifier = Modifier.size(28.dp),
                                icon = Icons.Filled.Delete,
                                actionText = "Delete Link",
                                color = LocalAppTheme.current.text,
                                backgroundColor = Color.Transparent,
                                shape = RectangleShape,
                                onClick = {
                                    onUiAction(EducationalMaterialUiAction.DeleteURL(index))
                                }
                            )
                        }
                    }
                }
                item {
                    if (canEdit && educationalMaterialIsCreated) {
                        app.presenter.components.buttons.IconButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(vertical = 14.dp),
                            icon = Icons.Filled.Add,
                            actionText = "Add Link",
                            color = LocalAppTheme.current.text,
                            backgroundColor = Color.Transparent,
                            shape = RectangleShape,
                            onClick = {
                                onUiAction(EducationalMaterialUiAction.AddURL)
                            }
                        )
                    }
                }
            }
        }
        if (canEdit && uiState.showSaveChangesButton) {
            app.presenter.components.buttons.TextButton(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter),
                text = if (educationalMaterialIsCreated) "Save Changes" else "Create Educational Material",
                color = LocalAppTheme.current.text,
                backgroundColor = LocalAppTheme.current.container,
                onClick = {
                    onUiAction(EducationalMaterialUiAction.SaveChanges)
                }
            )
        }
    }
}