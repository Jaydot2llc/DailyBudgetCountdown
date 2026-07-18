package com.jaydots.dailybudgetcountdown

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TWENTY_FOUR_HOURS_MILLIS = 24L * 60L * 60L * 1000L

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()
    private val transactionDao = AppDatabase.getDatabase(application).transactionDao()

    private val _currentBudget = MutableStateFlow<BudgetEntity?>(null)
    val currentBudget: StateFlow<BudgetEntity?> = _currentBudget.asStateFlow()

    /**
     * Saves [amount] into the budget table.
     * If a budget row was created within the last 24 hours, it is updated/replaced
     * in place. Otherwise a new row is inserted.
     * Calls [onSaved] with the row's id and the persisted amount once the write completes.
     */
    fun saveBudget(amount: Double, onSaved: (budgetId: Int, amount: Double) -> Unit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val cutoff = now - TWENTY_FOUR_HOURS_MILLIS
            val existingBudget = budgetDao.getActiveBudget(cutoff)

            val savedBudget: BudgetEntity
            if (existingBudget != null) {
                savedBudget = existingBudget.copy(
                    budgetDate = now,
                    totalBudget = amount,
                    remainingBudget = amount,
                    createdAt = now
                )
                budgetDao.update(savedBudget)
            } else {
                val newBudget = BudgetEntity(
                    budgetDate = now,
                    totalBudget = amount,
                    remainingBudget = amount,
                    createdAt = now
                )
                val newId = budgetDao.insert(newBudget).toInt()
                savedBudget = newBudget.copy(id = newId)
            }

            _currentBudget.value = savedBudget
            onSaved(savedBudget.id, savedBudget.totalBudget)
        }
    }

    /** Loads the budget row with [budgetId] into [currentBudget]. */
    fun loadBudget(budgetId: Int) {
        viewModelScope.launch {
            _currentBudget.value = budgetDao.getBudgetById(budgetId)
        }
    }

    /**
     * Records a transaction against [budgetId]: inserts a row into the transaction
     * table, then subtracts [cost] from that budget's remainingBudget and persists
     * the update. Calls [onComplete] once both writes finish.
     */
    fun recordTransaction(
        budgetId: Int,
        description: String,
        cost: Double,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()

            transactionDao.insert(
                TransactionEntity(
                    budgetId = budgetId,
                    cost = cost,
                    description = description,
                    createdAt = now
                )
            )

            val budget = budgetDao.getBudgetById(budgetId)
            if (budget != null) {
                val updatedBudget = budget.copy(remainingBudget = budget.remainingBudget - cost)
                budgetDao.update(updatedBudget)
                _currentBudget.value = updatedBudget
            }

            onComplete()
        }
    }
}