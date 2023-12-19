package com.example.investtrack

import SharedViewModel
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson


class DealActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var totalPortfolioValueTextView: TextView
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        val buttonHome: ImageButton = findViewById(R.id.button_home)
        val buttonDeals: ImageButton = findViewById(R.id.button_deals)
        val buttonAnalytics: ImageButton = findViewById(R.id.button_analytics)

        totalPortfolioValueTextView = findViewById(R.id.totalPortfolioValueTextView)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        sharedViewModel.totalPortfolioValue.observe(this) { totalPortfolioValue ->
            totalPortfolioValueTextView.text = "Общая стоимость портфеля: $totalPortfolioValue руб."
        }


        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
        updateDealsList()

        val btnAddDeal: Button = findViewById(R.id.btnAddDeal)
        btnAddDeal.setOnClickListener {
            showAddDealDialog()
        }

        buttonAnalytics.setOnClickListener {
            val totalPortfolioValue = totalPortfolioValueTextView.text.toString()
            val companyNames = mutableListOf<String>()
            val quantities = mutableListOf<Int>()

            for (deal in sharedViewModel.dealsList) {
                companyNames.add(deal.companyName ?: "")
                quantities.add(deal.quantity)
            }
            val intent = Intent(this, PortfolioActivity::class.java)

            intent.putStringArrayListExtra("companyNames", ArrayList(companyNames))
            intent.putIntegerArrayListExtra("quantities", ArrayList(quantities))
            intent.putExtra("totalPortfolioValue", totalPortfolioValue)

            startActivity(intent)
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

    private fun showAddDealDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Добавить сделку")

        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_deal, null)
        builder.setView(dialogView)

        val etCompanyName: EditText = dialogView.findViewById(R.id.etCompanyName)
        val etQuantity: EditText = dialogView.findViewById(R.id.etQuantity)
        val etPurchaseDate: EditText = dialogView.findViewById(R.id.etPurchaseDate)
        val etPricePerUnit: EditText = dialogView.findViewById(R.id.etPricePerUnit)

        builder.setPositiveButton("Добавить") { _, _ ->
            val companyName = etCompanyName.text.toString().trim()
            val quantity = etQuantity.text.toString().toInt()
            val purchaseDate = etPurchaseDate.text.toString().trim()
            val pricePerUnit = etPricePerUnit.text.toString().toDouble()

            val newDeal = Deal(
                companyName,
                quantity,
                purchaseDate,
                pricePerUnit
            )
            sharedViewModel.addDealAndUpdate(newDeal)
            updateDealsList()
        }
        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    fun buildDealString(deal: Deal): String {
        val companyName = deal.companyName ?:""
        val quantity = deal.quantity
        val purchaseDate = deal.purchaseDate
        val pricePerUnit = deal.pricePerUnit
        val all_price = deal.getTotalAmount()

        return "${deal.companyName} - ${deal.quantity} шт - общая стоимость: $all_price руб. ($purchaseDate)"
    }

    private fun updateDealsList() {
        val listView: ListView = findViewById(R.id.listView)
        adapter.clear()

        for (deal in sharedViewModel.dealsList) {
            val coloredDealString = getColoredDealString(deal)
            Log.d("DealList", "Colored com.example.investtrack.Deal String: $coloredDealString")
            adapter.add(coloredDealString.toString())
        }

        adapter.notifyDataSetChanged()


        listView.setOnItemClickListener { _, _, position, _ ->
            showDeleteDealDialog(position)
        }
    }

    private fun showDeleteDealDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Удалить сделку")
        builder.setMessage("Вы уверены, что хотите удалить эту сделку?")

        builder.setPositiveButton("Да") { _, _ ->
            sharedViewModel.removeDeal(sharedViewModel.dealsList[position])
            updateDealsList()
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun getColoredDealString(deal: Deal): SpannableString {
        val dealString = buildDealString(deal)
        val coloredDealString = SpannableString(dealString)
        val color = Color.RED
        coloredDealString.setSpan(ForegroundColorSpan(color), 0, dealString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return coloredDealString
    }

    override fun onPause() {
        super.onPause()
        saveDealsToSharedPreferences()
        sharedViewModel.saveTotalPortfolioValue(this)
    }

    override fun onResume() {
        super.onResume()
        loadDealsFromSharedPreferences()
        updateDealsList()
        sharedViewModel.loadTotalPortfolioValue(this)
    }

    private fun loadDataFromSharedPreferences() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val dealsJson = sharedPreferences.getString("deals", null)

        if (!dealsJson.isNullOrBlank()) {
            sharedViewModel.dealsList = Gson().fromJson(dealsJson, object : TypeToken<MutableList<Deal>>() {}.type)
        }

        sharedViewModel.loadTotalPortfolioValue(this)
    }

    private fun saveDataToSharedPreferences() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val dealsJson = Gson().toJson(sharedViewModel.dealsList)
        editor.putString("deals", dealsJson)

        sharedViewModel.saveTotalPortfolioValue(this)

        editor.apply()
    }

    private fun loadDealsFromSharedPreferences() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val dealsJson = sharedPreferences.getString("deals", null)

        if (!dealsJson.isNullOrBlank()) {
            sharedViewModel.dealsList = Gson().fromJson(dealsJson, object : TypeToken<MutableList<Deal>>() {}.type)
        }
    }

    private fun saveDealsToSharedPreferences() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val dealsJson = Gson().toJson(sharedViewModel.dealsList)
        editor.putString("deals", dealsJson)
        editor.apply()
    }
}