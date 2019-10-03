package br.com.osnirmesquita.blueshoes.util

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(typeFace: Typeface) : TypefaceSpan("") {

    private val newTypeFace = typeFace

    override fun updateDrawState(paint: TextPaint) {
        applyCustomTypeFace(paint, newTypeFace)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newTypeFace)
    }

    private fun applyCustomTypeFace(paint: TextPaint, typeface: Typeface) {
        val beforeTypeface = paint.typeface
        val beforeStyle = beforeTypeface?.style ?: 0

        val fake = beforeStyle and typeface.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25F
        }

        paint.typeface = typeface
    }
}