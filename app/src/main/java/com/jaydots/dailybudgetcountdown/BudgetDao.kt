package com.jaydots.dailybudgetcountdown

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {

    @Insert
    suspend fun insert(budget: BudgetEntity): Long

    @Update
    suspend fun update(budget: BudgetEntity): Unit

    /**
     * Returns the most recent budget row created within the last 24 hours (if any).
     * Pass System.currentTimeMillis() - 24 * 60 * 60 * 1000 as cutoffTimestamp.
     */
    @Query("SELECT * FROM budget WHERE createdAt >= :cutoffTimestamp ORDER BY createdAt DESC LIMIT 1")
    suspend fun getActiveBudget(cutoffTimestamp: Long): BudgetEntity?

    @Query("SELECT * FROM budget WHERE id = :budgetId LIMIT 1")
    suspend fun getBudgetById(budgetId: Int): BudgetEntity?
}
