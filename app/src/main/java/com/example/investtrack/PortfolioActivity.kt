package com.example.investtrack

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin


class PortfolioActivity : AppCompatActivity() {
    private lateinit var pieChart: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portfolio)

        val buttonHome: ImageButton = findViewById(R.id.button_home)
        val buttonDeals: ImageButton = findViewById(R.id.button_deals)
        val buttonAnalytics: ImageButton = findViewById(R.id.button_analytics)
        pieChart = findViewById(R.id.pieChart)

        buttonHome.setOnClickListener {
            val intent = Intent(this, PortfolioActivity::class.java)
            startActivity(intent)
        }
        buttonDeals.setOnClickListener {
            val intent = Intent(this, DealActivity::class.java)
            startActivity(intent)
        }

        val totalPortfolioValueTextView: TextView = findViewById(R.id.totalPortfolioValueTextView)
        val intent = intent
        val companyNames = intent.getStringArrayListExtra("companyNames")
        val quantities = intent.getIntegerArrayListExtra("quantities")
        val totalPortfolioValue = intent.getStringExtra("totalPortfolioValue")
        totalPortfolioValueTextView.text = totalPortfolioValue.toString()
        drawPieChart(companyNames, quantities)


    }

    private fun drawPieChart(companyNames: ArrayList<String>?, quantities: ArrayList<Int>?) {
        if (companyNames == null || quantities == null) return

        val size = minOf(companyNames.size, quantities.size)
        val hexColor1 = "#FF006F"
        val hexColor2 = "#638CF6"
        val hexColor3 = "#9E5971"
        val hexColor4 = "#EE005F"
        val color1 = Color.parseColor(hexColor1)
        val color2 = Color.parseColor(hexColor2)
        val color3 = Color.parseColor(hexColor3)
        val color4 = Color.parseColor(hexColor4)

        val colors = intArrayOf(color1, color2, color3, color4, Color.MAGENTA, Color.CYAN)

        val totalQuantity = quantities.sum()

        val pieChartBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(pieChartBitmap)

        val rectSize = 600

        var startAngle = 0f

        for (i in 0 until size) {
            val sweepAngle = (quantities[i].toFloat() / totalQuantity) * 360

            val paint = Paint()
            paint.color = colors[i % colors.size]
            paint.style = Paint.Style.FILL

            canvas.drawArc(0f, 0f, rectSize.toFloat(), rectSize.toFloat(), startAngle, sweepAngle, true, paint)

            // Ограничение длины названия компании до первых 5 символов
            val shortenedName = companyNames[i].substring(0, minOf(4, companyNames[i].length))
            // Расчет координат для текста
            val textX = (rectSize / 3 * cos(Math.toRadians(startAngle + sweepAngle / 3.toDouble()))).toFloat()
            val textY = (rectSize / 3 * sin(Math.toRadians(startAngle + sweepAngle / 3.toDouble()))).toFloat()

            // Определение параметров текста
            val textPaint = Paint()
            textPaint.color = Color.WHITE
            textPaint.textSize = 28f

            // Отображение текста
            canvas.drawText("$shortenedName\n" +
                    " ${"%.1f".format((quantities[i].toFloat() / totalQuantity) * 100)}%", (rectSize / 2 - textPaint.measureText(shortenedName) / 2) + textX, (rectSize / 2 + textY), textPaint)

            startAngle += sweepAngle
        }

        pieChart.setImageBitmap(pieChartBitmap)
    }
}

