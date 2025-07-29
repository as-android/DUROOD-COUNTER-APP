package com.example.duroodcounter.repositary

import com.example.duroodcounter.data.counteritemdata.RecitationDao
import com.example.duroodcounter.data.counteritemdata.RecitationDao.RecitationSum
import com.example.duroodcounter.data.counteritemdata.RecitationEntity
import com.example.duroodcounter.data.counterlistdata.CounterDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecitationRepository @Inject constructor(
    private val recitationDao: RecitationDao
) {
    suspend fun getCounterById(id: Int) = recitationDao.getCounterById(id)

    fun getRecitationsForCounter(counterId: Int): Flow<List<RecitationEntity>> =
        recitationDao.getRecitationsForCounter(counterId)

    suspend fun insertRecitation(entry: RecitationEntity) =
        recitationDao.insertRecitation(entry)
    suspend fun updateRecitationAmount(id: Int, amount: Int) =
        recitationDao.updateRecitationAmount(id,amount)

    suspend fun deleteRecitation(entry: RecitationEntity) =
        recitationDao.deleteRecitation(entry)

    suspend fun deleteByCounterId(counterId: Int) =
        recitationDao.deleteByCounterId(counterId)
    suspend fun deleteCounterById(counterId: Int) {
        recitationDao.deleteCounterById(counterId)
    }
    fun getRecitationSums(): Flow<List<RecitationSum>> = recitationDao.getRecitationSums()

}
