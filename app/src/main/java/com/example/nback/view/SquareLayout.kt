package com.example.nback.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * A LinearLayout with side equal to the minimum of the
 * width and height available for this view group (layout).
 * For details, see method onMeasure.
 *
 *
 * NB! Rebuild the project after adding a custom view or
 * view group to your project.
 */
class SquareLayout(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    // called when Android is trying to figure out the (new) size for this view
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val squareSide = Math.min(width, height)
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(squareSide, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(squareSide, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(squareSide, squareSide) // make it a square
    }
}