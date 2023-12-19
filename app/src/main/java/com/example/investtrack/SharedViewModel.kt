import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.investtrack.Deal

class SharedViewModel : ViewModel() {
    // Список сделок
    var dealsList = mutableListOf<Deal>()

    private val _totalPortfolioValue = MutableLiveData<Double>()
    val totalPortfolioValue: LiveData<Double> get() = _totalPortfolioValue

    // Метод для обновления общей стоимости
    fun updateTotalPortfolioValue() {
        val totalValue = dealsList.sumOf { it.getTotalAmount() }
        _totalPortfolioValue.value = totalValue
    }

    // Метод для добавления сделки
    fun addDealAndUpdate(deal: Deal) {
        dealsList.add(deal)
        updateTotalPortfolioValue()
    }

    // Метод для удаления сделки
    fun removeDeal(deal: Deal) {
        dealsList.remove(deal)
        updateTotalPortfolioValue()
    }

    fun saveTotalPortfolioValue(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("totalPortfolioValue", _totalPortfolioValue.value?.toFloat() ?: 0f)
        editor.apply()
    }
    fun loadTotalPortfolioValue(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        _totalPortfolioValue.value = sharedPreferences.getFloat("totalPortfolioValue", 0f).toDouble()
    }




}
