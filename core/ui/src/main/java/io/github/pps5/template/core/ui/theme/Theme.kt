package {{application_id}}.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Color(0xFF4CAF9F),
    primaryVariant = Color(0xFF00796B),
    secondary = Color(0xFF81D4FA),
    secondaryVariant = Color(0xFF0288D1),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFE53935),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF212121),
    onSurface = Color(0xFF212121),
    onError = Color(0xFFFFFFFF),
)

private val DarkColorPalette = darkColors(
    primary = Color(0xFF80CBC4),
    primaryVariant = Color(0xFF26A69A),
    secondary = Color(0xFF4BA3C7),
    secondaryVariant = Color(0xFF29B6F6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFEF5350),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onError = Color(0xFF000000)
)

@Composable
fun {{project_name}}Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ThemeColorPreview() {
    @Composable
    fun ColorItem(color: Color, text: String) {
        Surface(color = color, contentColor = contentColorFor(color)) {
            Text(text = text, style = MaterialTheme.typography.h6)
        }
    }

    {{project_name}}Theme {
        Surface {
            Column {
                ColorItem(MaterialTheme.colors.primary, "primary")
                ColorItem(MaterialTheme.colors.primaryVariant, "primaryVariant")
                ColorItem(MaterialTheme.colors.secondary, "secondary")
                ColorItem(MaterialTheme.colors.secondaryVariant, "secondaryVariant")
                ColorItem(MaterialTheme.colors.background, "background")
                ColorItem(MaterialTheme.colors.surface, "surface")
                ColorItem(MaterialTheme.colors.error, "error")
            }
        }
    }
}

@Preview
@Composable
private fun ThemeTypographyPreview() {
    {{project_name}}Theme {
        Surface {
            Column {
                Text("H1 Title", style = MaterialTheme.typography.h1)
                Text("H2 Title", style = MaterialTheme.typography.h2)
                Text("H3 Title", style = MaterialTheme.typography.h3)
                Text("H4 Title", style = MaterialTheme.typography.h4)
                Text("H5 Title", style = MaterialTheme.typography.h5)
                Text("H6 Title", style = MaterialTheme.typography.h6)
                Text("Subtitle 1", style = MaterialTheme.typography.subtitle1)
                Text("Subtitle 2", style = MaterialTheme.typography.subtitle2)
                Text("Body 1", style = MaterialTheme.typography.body1)
                Text("Body 2", style = MaterialTheme.typography.body2)
                Text("Button", style = MaterialTheme.typography.button)
                Text("Caption", style = MaterialTheme.typography.caption)
                Text("Overline", style = MaterialTheme.typography.overline)
            }
        }
    }
}

@Preview
@Composable
private fun ThemeShapePreview() {
    @Composable
    fun ShapeItem(shape: Shape, name: String) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(MaterialTheme.colors.primary)
                .size(100.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = name, color = MaterialTheme.colors.onPrimary)
        }
    }

    {{project_name}}Theme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShapeItem(MaterialTheme.shapes.small, "small")
                ShapeItem(MaterialTheme.shapes.medium, "medium")
                ShapeItem(MaterialTheme.shapes.large, "large")
            }
        }
    }
}
