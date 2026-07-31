package com.example.easymart.presentation.ui.common.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun OrderActionConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    icon: ImageVector,
    isDanger: Boolean,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val accentContainer = if (isDanger) appColors.errorContainer else appColors.infoContainer
    val accentContent = if (isDanger) appColors.error else appColors.info

    Dialog(
        onDismissRequest = {
            if (!isLoading) onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = !isLoading,
            dismissOnClickOutside = !isLoading
        )
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimens.radiusLarge),
            color = appColors.surfaceContainer,
            tonalElevation = dimens.cardElevation
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.spaceLg),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(dimens.iconLarge),
                        shape = CircleShape,
                        color = accentContainer
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(dimens.iconMedium),
                            tint = accentContent
                        )
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                content?.invoke()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(dimens.buttonHeight),
                        shape = RoundedCornerShape(dimens.radiusMedium)
                    ) {
                        Text(text = stringResource(R.string.ui_text_246))
                    }

                    Button(
                        onClick = onConfirm,
                        enabled = !isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(dimens.buttonHeight),
                        shape = RoundedCornerShape(dimens.radiusMedium),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDanger) appColors.error else MaterialTheme.colorScheme.primary,
                            contentColor = if (isDanger) appColors.onError else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimens.iconSmall),
                                strokeWidth = dimens.dividerThickness,
                                color = if (isDanger) appColors.onError else MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(text = confirmText, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderActionConfirmationDialogPreview() {
    EasyMartTheme {
        OrderActionConfirmationDialog(
            title = stringResource(R.string.ui_text_247),
            message = stringResource(R.string.ui_text_248),
            confirmText = stringResource(R.string.ui_text_249),
            icon = Icons.Rounded.WarningAmber,
            isDanger = true,
            isLoading = false,
            onDismiss = {},
            onConfirm = {}
        )
    }
}
