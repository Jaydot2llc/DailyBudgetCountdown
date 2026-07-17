package com.jaydots.dailybudgetcountdown


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single day's budget.
 * A new row is created when no active (< 24 hour old) budget exists;
 * otherwise the existing row is updated/replaced.
 */
@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val budgetDate: Long,        // epoch millis representing the calendar date this budget applies to
    val totalBudget: Double,
    val remainingBudget: Double,
    val createdAt: Long          // epoch millis timestamp of when this row was created/replaced
)
