package com.msm.mytestapp

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author Nazmul Hasan Masum
 */
class MainActivity : AppCompatActivity() {
    private var myView: IlluminationPieView? = null

    companion object {
        private const val TAG = "MainActivity"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie)
        myView = IlluminationPieView(this)
        val linearLayout = findViewById<LinearLayout>(R.id.linearOne)
        linearLayout.addView(myView)
        val seekBarX = findViewById<SeekBar>(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                myView!!.illuminate(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    inner class IlluminationPieView(context: Context?) : View(context) {
        private val paint: Paint = Paint()
        private var canvas: Canvas? = null
        private var radiusDynamic = 180

        private var x: Int = 0
        private var y: Int = 0
        private var pointF = PointF()

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            this.canvas = canvas
            x = width
            y = height
            val d = x / 2 - x / 8
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 10f
            paint.color = Color.BLACK
            canvas.drawPaint(paint)
            paint.style = Paint.Style.STROKE
            paint.color = Color.YELLOW
            paint.alpha = 50
            val path = Path()
            path.moveTo((x / 2 - (x / 2 - x / 8)).toFloat(), (y / 2).toFloat())

            for (i in 180 until radiusDynamic + 1) {
                pointF = getXYofAngle(i.toDouble(), d.toFloat())
                path.lineTo(pointF.x, pointF.y)
                path.lineTo(pointF.x, y / 2.toFloat())
            }
            canvas.drawPath(path, paint)
            paint.color = Color.DKGRAY
            val rectMain = RectF((x / 2 - (x / 2 - x / 8)).toFloat(), (y / 2 - (x / 2 - x / 8)).toFloat(), (x / 2 + (x / 2 - x / 8)).toFloat(), (y / 2 + (x / 2 - x / 8)).toFloat())
            canvas.drawArc(rectMain, 180f, 180f, true, paint)
            paint.style = Paint.Style.FILL
            paint.color = Color.rgb(254, 252, 215)
            canvas.drawCircle(pointF.x, pointF.y, 50f, paint)
        }

        private fun getXYofAngle(angle: Double, r: Float): PointF {
            val coordinate = PointF()
            val radians1 = Math.toRadians(angle)
            val cosAngle1 = cos(radians1)
            val sinAngle1 = sin(radians1)
            val pointX = (r * cosAngle1 + x / 2).toFloat()
            val pointY = (r * sinAngle1 + y / 2).toFloat()
            coordinate.x = pointX
            coordinate.y = pointY
            return coordinate
        }

        fun illuminate(progress: Int) {
            Log.d(TAG, "illuminate: $progress")
            if (progress > 0) {
                radiusDynamic = 180 + (180 * progress / 100)
                invalidate()
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (isInCircle(width / 2.toFloat(), height / 2.toFloat(), event, (width / 2 - width / 8).toFloat())) {
                    Log.d("Tap", "Inside the  Circle")
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                    if (isInCircle(width / 2.toFloat(), height / 2.toFloat(), event, (width / 2 - width / 8).toFloat())) {
                        Log.d("Tap", "Inside the  Circle")
                        Toast.makeText(context, "Click on the Circle", Toast.LENGTH_SHORT).show()
                    }
                    println(x + ' '.toFloat() + y)
                }
                MotionEvent.ACTION_CANCEL -> println(x + ' '.toFloat() + y)
            }
            return true
        }

        private fun isInCircle(cx: Float, cy: Float, tapPoint: MotionEvent, radius: Float): Boolean {
            val hype = sqrt((tapPoint.x - cx).toDouble().pow(2.0) + (tapPoint.y - cy).toDouble().pow(2.0)).toFloat()
            return radius >= hype
        }
    }

}