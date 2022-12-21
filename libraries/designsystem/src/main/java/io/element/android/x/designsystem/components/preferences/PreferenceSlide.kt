package io.element.android.x.designsystem.components.preferences

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import io.element.android.x.designsystem.components.preferences.components.PreferenceIcon
import io.element.android.x.designsystem.toEnabledColor

@Composable
fun PreferenceSlide(
    title: String,
    @FloatRange(0.0, 1.0)
    value: Float,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    summary: String? = null,
    steps: Int = 0,
    onValueChange: (Float) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = preferenceMinHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            PreferenceIcon(icon = icon)
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = enabled.toEnabledColor(),
                    text = title
                )
                summary?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = enabled.toEnabledColor(),
                        text = summary
                    )
                }
                Slider(
                    value = value,
                    steps = steps,
                    onValueChange = onValueChange,
                    enabled = enabled,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun PreferenceSlidePreview() {
    PreferenceSlide(
        title = "Slide",
        summary = "Summary",
        value = 0.75F
    )
}
