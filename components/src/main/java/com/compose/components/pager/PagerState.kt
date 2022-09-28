package com.compose.components.pager

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * State class to handle different properties for the Pager
 *
 * @see com.compose.components.pager.Pager
 */
class PagerState {
    var currentIndex by mutableStateOf(0)
    var numberOfItems by mutableStateOf(0)
    var itemFraction by mutableStateOf(0f)
    var overshootFraction by mutableStateOf(0f)
    var itemSpacing by mutableStateOf(0f)
    var itemDimension by mutableStateOf(0)
    var orientation by mutableStateOf(Orientation.Horizontal)
    var scope: CoroutineScope? by mutableStateOf(null)
    var listener: (Int) -> Unit by mutableStateOf({})
    val dragOffset = Animatable(0f)

    private val animationSpec = SpringSpec<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )

    suspend fun animateScrollToPage(page: Int) {
        dragOffset.snapTo(page.toFloat() * (itemDimension + itemSpacing))
    }

    val inputModifier = Modifier.pointerInput(numberOfItems) {
        fun itemIndex(offset: Int): Int = (offset / (itemDimension + itemSpacing)).roundToInt()
            .coerceIn(0, numberOfItems - 1)

        fun updateIndex(offset: Float) {
            val index = itemIndex(offset.roundToInt())
            if (index != currentIndex) {
                currentIndex = index
                listener(index)
            }
        }

        fun calculateOffsetLimit(): OffsetLimit {
            val dimension = when (orientation) {
                Orientation.Horizontal -> size.width
                Orientation.Vertical -> size.height
            }
            val itemSideMargin = (dimension - itemDimension) / 2f
            return OffsetLimit(
                min = -dimension * overshootFraction + itemSideMargin,
                max = numberOfItems * (itemDimension + itemSpacing) - (1f - overshootFraction) * dimension + itemSideMargin,
            )
        }

        forEachGesture {
            awaitPointerEventScope {
                val tracker = VelocityTracker()
                val decay = splineBasedDecay<Float>(this)
                val down = awaitFirstDown()
                val offsetLimit = calculateOffsetLimit()
                val dragHandler = { change: PointerInputChange ->
                    scope?.launch {
                        val dragChange = change.calculateDragChange(orientation)
                        dragOffset.snapTo(
                            (dragOffset.value - dragChange).coerceIn(
                                offsetLimit.min,
                                offsetLimit.max
                            )
                        )
                        updateIndex(dragOffset.value)
                    }
                    tracker.addPosition(change.uptimeMillis, change.position)
                }
                when (orientation) {
                    Orientation.Horizontal -> horizontalDrag(down.id, dragHandler)
                    Orientation.Vertical -> verticalDrag(down.id, dragHandler)
                }
                val velocity = tracker.calculateVelocity(orientation)
                scope?.launch {
                    var targetOffset = decay.calculateTargetValue(dragOffset.value, -velocity)
                    val remainder = targetOffset.toInt().absoluteValue % itemDimension
                    val extra = if (remainder > itemDimension / 2f) 1 else 0
                    val lastVisibleIndex = (targetOffset.absoluteValue / itemDimension.toFloat()).toInt() + extra
                    targetOffset = (lastVisibleIndex * (itemDimension + itemSpacing) * targetOffset.sign)
                        .coerceIn(0f, (numberOfItems - 1).toFloat() * (itemDimension + itemSpacing))
                    dragOffset.animateTo(
                        animationSpec = animationSpec,
                        targetValue = targetOffset,
                        initialVelocity = -velocity
                    ) {
                        updateIndex(value)
                    }
                }
            }
        }
    }

    data class OffsetLimit(
        val min: Float,
        val max: Float,
    )
}

/**
 * An extension function on VelocityTracker that will provide the calculated velocity based
 * on our scroll axis.
 */
private fun VelocityTracker.calculateVelocity(orientation: Orientation) = when (orientation) {
    Orientation.Horizontal -> calculateVelocity().x
    Orientation.Vertical -> calculateVelocity().y
}

/**
 * An extension function calculates the pointer input change based on our scroll direction.
 */
private fun PointerInputChange.calculateDragChange(orientation: Orientation) =
    when (orientation) {
        Orientation.Horizontal -> positionChange().x
        Orientation.Vertical -> positionChange().y
    }