package com.skifire13.solversequenti.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.skifire13.solversequenti.R
import com.skifire13.solversequenti.solver.*
import kotlin.math.max

class DerivazioneView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var derivazione: Derivazione? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        if(isInEditMode) // Debug
            derivazione = parse("(!B->G)&(G->!B)|-")?.derivazione()
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = resources.getFont(R.font.computermodern_custom)
        textSize = textHeight.toFloat()
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = lineStrokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measure = derivazione?.measure() ?: return super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(
            resolveSizeAndState(paddingLeft + paddingRight + measure.width, widthMeasureSpec, 0),
            resolveSizeAndState(paddingBottom + paddingTop + measure.height, heightMeasureSpec, 0)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        derivazione?.draw(canvas, 0f + paddingLeft, 0f + paddingTop)

    }

    private fun Derivazione.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.apply {
            drawText(sequenteText, x + measure.seqStart, y + measure.height - textPaint.fontMetrics.descent, textPaint)

            when(albero) {
                is Stop -> {
                    if(regola != Regola.NonDerivabile)
                        drawText(regolaText, x + measure.ramoStart, y + textHeight, textPaint)
                }
                is Ramo -> {
                    drawLine(
                        x + measure.lineStart,
                        y + measure.height - textHeight - paddingBottomLine,
                        x + measure.lineEnd,
                        y + measure.height - textHeight - paddingBottomLine,
                        linePaint)
                    drawText(regolaText,
                        x + measure.lineEnd + paddingLeftRegola,
                        y + measure.height - textHeight - paddingBottomLine + textHeight/2,
                        textPaint)
                    albero.ramo.draw(canvas, x + measure.ramoStart, y)
                }
                is Rami -> {
                    drawLine(
                        x + measure.lineStart,
                        y + measure.height - textHeight - paddingBottomLine,
                        x + measure.lineEnd,
                        y + measure.height - textHeight - paddingBottomLine,
                        linePaint)
                    drawText(regolaText,
                        x + measure.lineEnd + paddingLeftRegola,
                        y + measure.height - textHeight - paddingBottomLine + textHeight/2,
                        textPaint)

                    val heightDiff = albero.ramoSx.measure.height - albero.ramoDx.measure.height
                    albero.ramoSx.draw(canvas, x + measure.ramoStart, y + if(heightDiff > 0) 0 else -heightDiff)
                    albero.ramoDx.draw(canvas, x + measure.ramoStart + albero.ramoSx.measure.width + paddingRami, y + if(heightDiff < 0) 0 else heightDiff)
                }
            }
        }
    }

    private fun Derivazione.measure(): Measure {
        val seqWidth = textPaint.measureText(sequenteText + "\u2009").toInt()
        val regolaWidth = textPaint.measureText(regolaText + "\u2009").toInt()
        val textHeight = textPaint.fontMetricsInt.let { it.descent - it.ascent }

        val bounds = Rect()
        textPaint.getTextBounds(regolaText, 0, regolaText.length, bounds)

        val width: Int
        val height: Int
        val lineStart: Int
        val lineEnd: Int
        val seqStart: Int
        val seqEnd: Int
        val ramoStart: Int

        when(albero) {
            is Stop -> {
                lineStart = 0
                lineEnd = 0

                if(regola == Regola.NonDerivabile) {
                    width = seqWidth
                    height = textHeight
                    seqStart = 0
                    ramoStart = 0
                } else {
                    width = max(seqWidth, regolaWidth)
                    height = 2 * textHeight + paddingBottomAssioma
                    if(seqWidth > regolaWidth) {
                        seqStart = 0
                        ramoStart = (seqWidth - regolaWidth) / 2
                    } else {
                        ramoStart = 0
                        seqStart = (regolaWidth - seqWidth) / 2
                    }
                }
                seqEnd = seqStart + seqWidth
            }
            is Ramo -> {
                val ramoMeasure = albero.ramo.measure()

                val baseWidth = max(seqWidth + 2 * paddingSideLine, max(ramoMeasure.width, ramoMeasure.seqWidth + 2 * paddingSideLine))
                height = ramoMeasure.height + paddingTopLine + lineStrokeWidth.toInt() + paddingBottomLine + textHeight

                if(seqWidth > ramoMeasure.width) {
                    ramoStart = (seqWidth - ramoMeasure.width) / 2 + paddingSideLine

                    seqStart = paddingSideLine
                    lineStart = 0
                    lineEnd = seqStart + seqWidth + paddingSideLine
                } else {
                    val lineWidth = max(seqWidth, ramoMeasure.seqWidth) + 2 * paddingSideLine
                    val midRamoSeq = (ramoMeasure.seqEnd + ramoMeasure.seqStart)/2

                    ramoStart = max(0, lineWidth/2 - midRamoSeq)
                    seqStart = ramoStart + midRamoSeq - seqWidth / 2

                    lineStart = ramoStart + midRamoSeq - lineWidth / 2
                    lineEnd = lineStart + lineWidth
                }
                seqEnd = seqStart + seqWidth
                width = max(baseWidth, lineEnd + paddingLeftRegola + regolaWidth)
            }
            is Rami -> {
                val ramoSxMeasure = albero.ramoSx.measure()
                val ramoDxMeasure = albero.ramoDx.measure()

                val baseWidth = ramoSxMeasure.width + paddingRami + ramoDxMeasure.width + if(ramoSxMeasure.seqStart < paddingSideLine) paddingSideLine - ramoSxMeasure.seqStart else 0
                height = max(ramoSxMeasure.height, ramoDxMeasure.height) + paddingTopLine + lineStrokeWidth.toInt() + paddingBottomLine + textHeight
                lineStart = if(ramoSxMeasure.seqStart < paddingSideLine) 0 else { ramoSxMeasure.seqStart - paddingSideLine }
                lineEnd = ramoSxMeasure.width + paddingRami + ramoDxMeasure.seqEnd + paddingSideLine
                width = max(baseWidth, lineEnd + paddingLeftRegola + regolaWidth)
                seqStart = lineStart + ((lineEnd - lineStart) - seqWidth)/2 + paddingSideLine
                seqEnd = seqStart + seqWidth

                ramoStart = if(ramoSxMeasure.seqStart < paddingSideLine) paddingSideLine - ramoSxMeasure.seqStart else 0
            }
        }

        measure = Measure(
            width = width,
            height = height,
            lineStart = lineStart,
            lineEnd = lineEnd,
            seqStart = seqStart,
            seqEnd = seqEnd,
            ramoStart = ramoStart
        )

        return measure
    }
}

const val paddingBottomAssioma = 0
const val paddingLeftRegola = 10
const val paddingRami = 75
const val paddingTopLine = -15
const val paddingBottomLine = 30
const val paddingSideLine = 10
const val lineStrokeWidth = 3f
const val textHeight = 60

data class Measure(
    val width: Int,
    val height: Int,
    val lineStart: Int,
    val lineEnd: Int,
    val seqStart: Int,
    val seqEnd: Int,
    val ramoStart: Int
) {
    val seqWidth get() = seqEnd - seqStart
}
