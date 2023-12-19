package com.example.investtrack

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DealTest {
    @Test
    fun testGetTotalAmount() {
        val deal = Deal("Company TEST", 10, "01/12/2023", 50.0)

        deal.companyName = "Company TEST"
        deal.quantity = 10
        deal.purchaseDate = "01/12/2023"
        deal.pricePerUnit = 50.0

        val expectedAmount = 10 * 50.0
        val actualAmount = deal.getTotalAmount()

        assertEquals(expectedAmount, actualAmount, 0.01)
    }


}
