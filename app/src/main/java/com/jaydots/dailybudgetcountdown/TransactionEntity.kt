package com.jaydots.dailybudgetcountdown

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single spend against a budget.
 * "transaction" is a reserved SQL keyword, but Room automatically wraps
 * table/column identifiers in backticks when generating SQL, so this is safe.
 */
@Entity(
    tableName = "transaction",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("budgetId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val budgetId: Int,
    val cost: Double,
    val description: String,
    val createdAt: Long
)
