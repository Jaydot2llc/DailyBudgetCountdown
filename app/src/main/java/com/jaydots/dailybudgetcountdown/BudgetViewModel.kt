package com.jaydots.dailybudgetcountdown

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TWENTY_FOUR_HOURS_MILLIS = 24L * 60L * 60L * 1000L

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()

    /**
     * Saves [amount] into the budget table.
     * If a budget row was created within the last 24 hours, it is updated/replaced
     * in place. Otherwise a new row is inserted.
     * Calls [onSaved] with the persisted amount once the write completes.
     */
    fun saveBudget(amount: Double, onSaved: (Double) -> Unit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val cutoff = now - TWENTY_FOUR_HOURS_MILLIS
            val existingBudget = budgetDao.getActiveBudget(cutoff)

            if (existingBudget != null) {
                budgetDao.update(
                    existingBudget.copy(
                        budgetDate = now,
                        totalBudget = amount,
                        remainingBudget = amount,
                        createdAt = now
                    )
                )
            } else {
                budgetDao.insert(
                    BudgetEntity(
                        budgetDate = now,
                        totalBudget = amount,
                        remainingBudget = amount,
                        createdAt = now
                    )
                )
            }

            onSaved(amount)
        }
    }
}
