package com.compose.modifiers.shapes

import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import com.compose.modifiers.internal.BlurMaker

/**
 * Represents neumorphic shape
 */
interface NeuShape {

    fun drawShadows(drawScope: ContentDrawScope, blurMaker: BlurMaker, shapeConfig: ShapeConfig)
}