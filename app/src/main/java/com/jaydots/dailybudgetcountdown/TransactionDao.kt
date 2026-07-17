package com.jaydots.dailybudgetcountdown
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Query("SELECT * FROM `transaction` WHERE budgetId = :budgetId ORDER BY createdAt DESC")
    suspend fun getTransactionsForBudget(budgetId: Int): List<TransactionEntity>
}