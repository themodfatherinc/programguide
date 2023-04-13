package eu.wewox.minabox

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection

/**
 * Interface to provide offset of the registered items.
 */
public interface MinaBoxPositionProvider {

    /**
     * Returns offset of the item with global [index].
     *
     * @param index The global index of the item.
     * @param alignment The alignment to align item inside the [MinaBox].
     * @param paddingStart An additional start padding to tweak alignment.
     * @param paddingTop An additional top padding to tweak alignment.
     * @param paddingEnd An additional end padding to tweak alignment.
     * @param paddingBottom An additional bottom padding to tweak alignment.
     * @return An item offset.
     */
    public fun getOffset(
        index: Int,
        alignment: Alignment = Alignment.Center,
        paddingStart: Float = 0f,
        paddingTop: Float = 0f,
        paddingEnd: Float = 0f,
        paddingBottom: Float = 0f,
    ): Offset
}

/**
 * Implementation of the [MinaBoxPositionProvider].
 *
 * @property items All items registered in [MinaBoxScope].
 * @property layoutDirection Current layout direction.
 * @property size The size of the viewport.
 */
internal class MinaBoxPositionProviderImpl(
    private val items: Map<Int, MinaBoxItem>,
    private val layoutDirection: LayoutDirection,
    private val size: Size,
) : MinaBoxPositionProvider {

    override fun getOffset(
        index: Int,
        alignment: Alignment,
        paddingStart: Float,
        paddingTop: Float,
        paddingEnd: Float,
        paddingBottom: Float,
    ): Offset =
        getOffset(
            index = index,
            alignment = alignment,
            paddingStart = paddingStart,
            paddingTop = paddingTop,
            paddingEnd = paddingEnd,
            paddingBottom = paddingBottom,
            currentX = 0f,
            currentY = 0f
        )

    /**
     * Returns offset of the item with global [index].
     *
     * @param index The global index of the item.
     * @param alignment The alignment to align item inside the [MinaBox].
     * @param paddingStart An additional start padding to tweak alignment.
     * @param paddingTop An additional top padding to tweak alignment.
     * @param paddingEnd An additional end padding to tweak alignment.
     * @param paddingBottom An additional bottom padding to tweak alignment.
     * @param currentX The current offset on the X axis.
     * @param currentY The current offset on the Y axis.
     * @return An item offset.
     */
    fun getOffset(
        index: Int,
        alignment: Alignment,
        paddingStart: Float,
        paddingTop: Float,
        paddingEnd: Float,
        paddingBottom: Float,
        currentX: Float,
        currentY: Float,
    ): Offset {
        val info = items[index] ?: return Offset.Zero
        val offset = alignment.align(
            size = IntSize(
                info.width.resolve().toInt(),
                info.height.resolve().toInt(),
            ),
            space = IntSize(
                (size.width - paddingStart - paddingEnd).toInt(),
                (size.height - paddingTop - paddingBottom).toInt(),
            ),
            layoutDirection = layoutDirection,
        )
        return Offset(
            if (info.lockHorizontally) currentX else (info.x - offset.x - paddingStart),
            if (info.lockVertically) currentY else (info.y - offset.y - paddingTop),
        )
    }
}
