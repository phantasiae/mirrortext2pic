package com.example.text2image

import android.content.Context
import android.graphics.*
import android.view.View

class TextPic(context: Context, view: View) {

    private val height = 383
    private val width = 900
    lateinit var text: String
    private var context: Context = context
    private var view: View = view

    fun generatePicture(): Bitmap? {
        val textPictured = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(textPictured).also {
            val paint1 = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.BLACK
                strokeWidth = 40f
                style = Paint.Style.FILL

                textSize = 76f
                isFilterBitmap = true
                clearShadowLayer()
                isDither = true
            }

            val wordsLength = paint1.measureText(text)
            it.drawARGB(255, 255, 255, 255);
            if (wordsLength < 900) {
                it.drawText(text, 0, text.length, 80f, 80f + 40f, paint1)
            }
            drawMultiLineText(it, paint1, text)
        }

        val matrix = Matrix()
        matrix.setRotate(180f, (height / 2.0).toFloat(), (width / 2.0).toFloat())
        return Bitmap.createBitmap(textPictured, 0, 0, width, height, matrix, true)
    }

    private fun drawMultiLineText(canvas: Canvas, paint: Paint, text: String) {
        var privousSplitPoint = 0;
        var multiLinetexts = listOf<String>();
        for((index, char) in text.withIndex()) {
            val line = text.substring(privousSplitPoint, index)
            if (paint.measureText(line) > this.width - 170f) {
                multiLinetexts += line
                privousSplitPoint = index
                val lineHight = multiLinetexts.size * 80f
                canvas.drawText(line, 0, line.length, 80f,  lineHight+ 40f, paint)
            }
        }
    }
}
