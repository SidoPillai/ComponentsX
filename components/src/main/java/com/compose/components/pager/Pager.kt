package com.compose.components.pager

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * A ViewPager composable which can scroll horizontally and vertically.
 *
 *  [Reference](https://fvilarino.medium.com/creating-a-viewpager-in-jetpack-compose-332d6a9181a5)
 *
 * @param items Items of the same type
 * @param modifier the modifier to be applied to this layout
 * @param orientation specify the orientation for scroll
 * @param initialIndex the item that will be centered initially
 * @param itemFraction indicates which fraction of the overall width or height of the container
 * the items will take. This fraction defaults to 1, so items will be as wide (horizontal scroll)
 * or as tall (vertical scroll) as the container if no value is provided.
 * @param itemSpacing indicates, in DPs, the space between items
 * @param overshootFraction indicates, as a fraction of the parent width or height, how much
 * the first and last items can be scrolled off position. A default value of 0.5f means the first
 * and last items can be pushed so that they are half way off from their resting positions.
 * @param onItemSelect callback that will get called whenever an item becomes selected
 * (in the central position). This will allow clients of the Pager to be notified of scroll events.
 * @param contentFactory this will build the items for our Pager — this factory will be called with
 * the items in the list provided as the 1st argument and for each item we will receive a Composable
 * that we will display in the Pager.
 * @param state Remembers the pager state
 *
 * @author Siddesh
 */
@Composable
fun <T : Any> Pager(
    items: List<T>,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    initialIndex: Int = 0,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    itemFraction: Float = 1f,
    itemSpacing: Dp = 0.dp,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    overshootFraction: Float = .5f,
    onItemSelect: (T) -> Unit = {},
    contentFactory: @Composable (T) -> Unit,
    state: PagerState = rememberPagerState()
) {
    require(initialIndex in 0..items.lastIndex) { "Initial index out of bounds" }
    require(itemFraction > 0f && itemFraction <= 1f) { "Item fraction must be in the (0f, 1f] range" }
    require(overshootFraction > 0f && itemFraction <= 1f) { "Overshoot fraction must be in the (0f, 1f] range" }
    val scope = rememberCoroutineScope()
    state.currentIndex = initialIndex
    state.numberOfItems = items.size
    state.itemFraction = itemFraction
    state.overshootFraction = overshootFraction
    state.itemSpacing = with(LocalDensity.current) { itemSpacing.toPx() }
    state.orientation = orientation
    state.listener = { index -> onItemSelect(items[index]) }
    state.scope = scope

    Layout(
        content = {
            items.map { item ->
                Box(
                    modifier = when (orientation) {
                        Orientation.Horizontal -> Modifier.fillMaxWidth()
                        Orientation.Vertical -> Modifier.fillMaxHeight()
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    contentFactory(item)
                }
            }
        },
        modifier = modifier
            .clipToBounds()
            .then(state.inputModifier),
    ) { measurables, constraints ->
        // Using helper method - check the orientation and we get the max width or height from the Constraints
        val dimension = constraints.dimension(orientation)

        // Using helper method - make a copy of our incoming Constraints with the min and max on
        // the scroll direction set to the same value, and the min on the other direction set to 0.
        val looseConstraints = constraints.toLooseConstraints(orientation, state.itemFraction)

        // measure the content using loose constraints
        val placeables = measurables.map { measurable -> measurable.measure(looseConstraints) }

        // Check the max size (height for horizontal or width for vertical scroll)
        val size = placeables.getSize(orientation, dimension)

        // Calculate how tall or wide the children will be from the item fraction
        val itemDimension = (dimension * state.itemFraction).roundToInt()
        state.itemDimension = itemDimension
        val halfItemDimension = itemDimension / 2

        layout(size.width, size.height) {

            // As we want to place the selected item in the center of the container, we calculate
            // the offset an item will need to display centered.
            val centerOffset = dimension / 2 - halfItemDimension

            val dragOffset = state.dragOffset.value
            val roundedDragOffset = dragOffset.roundToInt()
            val spacing = state.itemSpacing.roundToInt()
            val itemDimensionWithSpace = itemDimension + state.itemSpacing

            // We use the drag offset to calculate what will be the first and last visible items in
            // the Pager, this will allow us to optimize the rendering by only displaying those
            // items that are visible.
            val first = ceil(
                (dragOffset -itemDimension - centerOffset) / itemDimensionWithSpace
            ).toInt().coerceAtLeast(0)
            val last = ((dimension + dragOffset - centerOffset) / itemDimensionWithSpace).toInt()
                .coerceAtMost(items.lastIndex)

            for (i in first..last) {

                // For each item we calculate its position based on how much the user has dragged
                // the content. There is a bit of math involved here, but basically the offset for
                // an item is the size of the items preceding it (including the separator), minus
                // the drag offset, plus the offset needed to center an item on the screen.
                // It’s important to note here that whenever the dragOffset updates the Pager will
                // recompose, this is what we will use for dragging and flinging.
                val offset = i * (itemDimension + spacing) - roundedDragOffset + centerOffset

                // place each item. If we are scrolling horizontally the horizontal (x) coordinate
                // is our calculated offset and the vertical coordinate (y) is 0. Otherwise, if we
                // are scrolling vertically, our horizontal coordinate is 0 and the vertical
                // coordinate is our offset.
                placeables[i].place(
                    x = when (orientation) {
                        Orientation.Horizontal -> offset
                        Orientation.Vertical -> 0
                    },
                    y = when (orientation) {
                        Orientation.Horizontal -> 0
                        Orientation.Vertical -> offset
                    }
                )
            }
        }
    }

    LaunchedEffect(key1 = items, key2 = initialIndex) {
        state.animateScrollToPage(initialIndex)
    }
}

/**
 * Remembers the pager state value
 */
@Composable
fun rememberPagerState(): PagerState = remember { PagerState() }

/**
 * Returns the maximum dimension on our Constraints based on our scroll orientation.
 *
 * @param orientation
 */
private fun Constraints.dimension(orientation: Orientation) = when (orientation) {
    Orientation.Horizontal -> maxWidth
    Orientation.Vertical -> maxHeight
}

/**
 * Makes a copy of our Constraints where the minimum dimension not in the scroll direction
 * is set to 0, while on the scroll direction we set the minimum dimension to be equal to the
 * maximum dimension. Note also that we are making use of the itemFraction to constrain the
 * Pager items to a fraction of the container dimension in the scroll direction.
 *
 * @param orientation
 * @param itemFraction
 */
private fun Constraints.toLooseConstraints(
    orientation: Orientation,
    itemFraction: Float,
): Constraints {
    val dimension = dimension(orientation)
    return when (orientation) {
        Orientation.Horizontal -> copy(
            minWidth = (dimension * itemFraction).roundToInt(),
            maxWidth = (dimension * itemFraction).roundToInt(),
            minHeight = 0,
        )
        Orientation.Vertical -> copy(
            minWidth = 0,
            minHeight = (dimension * itemFraction).roundToInt(),
            maxHeight = (dimension * itemFraction).roundToInt(),
        )
    }
}

/**
 * gets the size of our Parcelables based on the scroll direction. If we are scrolling horizontally,
 * then we want to know the maximum height of the Pager items; otherwise, if we are scrolling
 * vertically, we want to calculate the maximum width.
 *
 * @param orientation
 * @param dimension
 */
private fun List<Placeable>.getSize(
    orientation: Orientation,
    dimension: Int,
): IntSize {
    return when (orientation) {
        Orientation.Horizontal -> IntSize(
            dimension,
            maxByOrNull { it.height }?.height ?: 0
        )
        Orientation.Vertical -> IntSize(
            maxByOrNull { it.width }?.width ?: 0,
            dimension
        )
    }
}