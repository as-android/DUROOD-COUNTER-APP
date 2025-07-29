package com.example.duroodcounter.repositary

import com.example.duroodcounter.data.counterlistdata.CounterDao
import com.example.duroodcounter.data.counterlistdata.CounterEntity
import javax.inject.Inject

class CounterRepository @Inject constructor(
    private val dao: CounterDao
) {
    val counters = dao.getAllCounters()

    suspend fun addCounter(counter: CounterEntity) = dao.insertCounter(counter)

    suspend fun deleteCounter(counter: CounterEntity) = dao.deleteCounter(counter)
    suspend fun deleteAllCounters() = dao.deleteAllCounters()
}
