package com.example.duroodcounter.data.counterlistdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    @Query("SELECT * FROM counters ORDER BY id DESC")
    fun getAllCounters(): Flow<List<CounterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counter: CounterEntity)

    @Delete
    suspend fun deleteCounter(counter: CounterEntity)

    @Query("DELETE FROM counters")
    suspend fun deleteAllCounters()


}
