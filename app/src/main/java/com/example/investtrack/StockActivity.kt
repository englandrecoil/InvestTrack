package com.example.investtrack

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StockActivity : AppCompatActivity() {
    lateinit var tvDate: TextView
    lateinit var buttonDate: Button
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        tvDate = findViewById(R.id.selected_date)
        buttonDate = findViewById(R.id.button_date)
        val buttonHome: ImageButton = findViewById(R.id.button_home)
        val buttonDeals: ImageButton = findViewById(R.id.button_deals)
        val buttonAnalytics: ImageButton = findViewById(R.id.button_analytics)


        buttonDate.setOnClickListener{
            showDatePicker()
        }
        buttonHome.setOnClickListener {
            val intent = Intent(this, PortfolioActivity::class.java)
            startActivity(intent)
        }
        buttonDeals.setOnClickListener {
            val intent = Intent(this, DealActivity::class.java)
            startActivity(intent)
        }
    }


    fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year,monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            tvDate.text = formattedDate
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}


