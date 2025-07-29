package com.example.duroodcounter.data.counteritemdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.duroodcounter.data.counterlistdata.CounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecitationDao {

    @Query("SELECT * FROM counters WHERE id = :id")
    suspend fun getCounterById(id: Int): CounterEntity

    @Query("SELECT * FROM recitations WHERE counterId = :counterId ORDER BY timestamp DESC")
    fun getRecitationsForCounter(counterId: Int): Flow<List<RecitationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecitation(entry: RecitationEntity): Long

    @Query("UPDATE recitations SET amount = :amount WHERE id = :id")
    suspend fun updateRecitationAmount(id: Int, amount: Int)

    @Delete
    suspend fun deleteRecitation(entry: RecitationEntity)

    @Query("DELETE FROM recitations WHERE counterId = :counterId")
    suspend fun deleteByCounterId(counterId: Int)

    @Query("DELETE FROM counters WHERE id = :counterId")
    suspend fun deleteCounterById(counterId: Int)

    @Query("SELECT counterId, SUM(amount) as totalRecited FROM recitations GROUP BY counterId")
    fun getRecitationSums(): Flow<List<RecitationSum>>

    data class RecitationSum(
        val counterId: Int,
        val totalRecited: Int
    )
}


