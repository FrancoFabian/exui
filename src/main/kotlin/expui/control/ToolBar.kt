package expui.control

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import expui.style.AreaColors
import expui.style.AreaProvider
import expui.style.DisabledAreaProvider
import expui.style.HoverAreaProvider
import expui.style.InactiveSelectionAreaProvider
import expui.style.LocalAreaColors
import expui.style.LocalDisabledAreaColors
import expui.style.LocalHoverAreaColors
import expui.style.LocalInactiveAreaColors
import expui.style.LocalNormalAreaColors
import expui.style.LocalPressedAreaColors
import expui.style.LocalSelectionAreaColors
import expui.style.LocalSelectionInactiveAreaColors
import expui.style.PressedAreaProvider
import expui.style.areaBackground
import expui.theme.LightTheme

class ToolBarActionButtonColors(
    override val normalAreaColors: AreaColors,
    override val hoverAreaColors: AreaColors,
    override val pressedAreaColors: AreaColors,
    override val disabledAreaColors: AreaColors,
    override val selectionAreaColors: AreaColors,
    override val inactiveAreaColors: AreaColors,
    override val inactiveSelectionAreaColors: AreaColors,
) : AreaProvider, HoverAreaProvider, PressedAreaProvider, DisabledAreaProvider, InactiveSelectionAreaProvider {
    @Composable
    fun provideArea(enabled: Boolean, selected: Boolean, content: @Composable () -> Unit) {
        val activated = LocalContentActivated.current
        val currentColors = when {
            !enabled -> disabledAreaColors
            selected -> if (activated) selectionAreaColors else inactiveSelectionAreaColors
            !activated -> inactiveAreaColors
            else -> normalAreaColors
        }
        CompositionLocalProvider(
            LocalAreaColors provides currentColors,
            LocalNormalAreaColors provides normalAreaColors,
            LocalDisabledAreaColors provides disabledAreaColors,
            LocalHoverAreaColors provides hoverAreaColors,
            LocalPressedAreaColors provides pressedAreaColors,
            LocalSelectionInactiveAreaColors provides inactiveSelectionAreaColors,
            LocalInactiveAreaColors provides inactiveAreaColors,
            LocalSelectionAreaColors provides selectionAreaColors,
            content = content
        )
    }
}

val LocalToolBarActionButtonColors = compositionLocalOf {
    LightTheme.ToolBarActionButtonColors
}

@Composable
fun ToolBarActionButton(
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(6.dp),
    indication: Indication? = HoverOrPressedIndication(shape),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: ToolBarActionButtonColors = LocalToolBarActionButtonColors.current,
    content: @Composable BoxScope.() -> Unit,
) {
    colors.provideArea(enabled, selected) {
        Box(
            modifier.clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick,
                role = Role.Button
            ).areaBackground(shape = shape),
            propagateMinConstraints = true
        ) {
            content()
        }
    }
}
