package com.example.investtrack
data class Deal(
    var companyName: String?,
    var quantity: Int,
    var purchaseDate: String,
    var pricePerUnit: Double
) {
    fun getTotalAmount(): Double {
        return quantity * pricePerUnit
    }
}