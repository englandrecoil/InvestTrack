package com.example.investtrack

import SharedViewModel
import android.widget.Button
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)

class DealActivityTest {

    private lateinit var activity: DealActivity
    private lateinit var viewModel: SharedViewModel

    @Before
    fun setUp() {
        val scenario = ActivityScenario.launch(DealActivity::class.java)
        scenario.onActivity { currentActivity ->
            activity = currentActivity
            viewModel = mock(SharedViewModel::class.java)
            activity.sharedViewModel = viewModel
        }
    }

    @Test
    fun testActivityNotNull() {
        assertNotNull(activity)
    }

    @Test
    fun testAddDealToList() {
        val initialDealCount = activity.sharedViewModel.dealsList.size

        val btnAddDeal: Button = activity.findViewById(R.id.btnAddDeal)
        btnAddDeal.performClick()

        val etCompanyName: EditText = activity.findViewById(R.id.etCompanyName)
        etCompanyName.setText("Test Company")

        val etQuantity: EditText = activity.findViewById(R.id.etQuantity)
        etQuantity.setText("10")

        val etPurchaseDate: EditText = activity.findViewById(R.id.etPurchaseDate)
        etPurchaseDate.setText("2023-01-01")

        val etPricePerUnit: EditText = activity.findViewById(R.id.etPricePerUnit)
        etPricePerUnit.setText("20.0")

        val addButton: Button = activity.findViewById(R.id.btnAddDeal)
        addButton.performClick()

        val updatedDealCount = activity.sharedViewModel.dealsList.size
        assertEquals("Deal should be added to the list", initialDealCount + 1, updatedDealCount)
    }

    @Test
    fun testUpdateTotalPortfolioValue() {
        val addButton: Button = activity.findViewById(R.id.btnAddDeal)
        addButton.performClick()

        val etCompanyName: EditText = activity.findViewById(R.id.etCompanyName)
        etCompanyName.setText("Test Company")

        val etQuantity: EditText = activity.findViewById(R.id.etQuantity)
        etQuantity.setText("10")

        val etPurchaseDate: EditText = activity.findViewById(R.id.etPurchaseDate)
        etPurchaseDate.setText("2023-01-01")

        val etPricePerUnit: EditText = activity.findViewById(R.id.etPricePerUnit)
        etPricePerUnit.setText("20.0")

        addButton.performClick()

        verify(viewModel).updateTotalPortfolioValue()
    }
}
