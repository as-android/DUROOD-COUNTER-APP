package com.example.duroodcounter.screens.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duroodcounter.data.counteritemdata.RecitationEntity
import com.example.duroodcounter.data.counterlistdata.CounterEntity
import com.example.duroodcounter.repositary.RecitationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterDetailsViewModel @Inject constructor(
    private val repository: RecitationRepository
) : ViewModel() {

    private val _counter = MutableStateFlow<CounterEntity?>(null)
    val counter: StateFlow<CounterEntity?> = _counter.asStateFlow()

    private val _recitations = MutableStateFlow<List<RecitationEntity>>(emptyList())
    val recitations: StateFlow<List<RecitationEntity>> = _recitations.asStateFlow()

    private var currentCounterId: Int? = null

    fun loadCounterWithHistory(counterId: Int) {
        currentCounterId = counterId

        viewModelScope.launch {
            _counter.value = repository.getCounterById(counterId)
        }

        viewModelScope.launch {
            repository.getRecitationsForCounter(counterId).collect {
                _recitations.value = it
            }
        }
    }

    fun addRecitation(counterId: Int, amount: Int) {
        viewModelScope.launch {
            val newEntry = RecitationEntity(
                counterId = counterId,
                amount = amount,
                timestamp = System.currentTimeMillis()
            )
            repository.insertRecitation(newEntry)
        }
    }
    fun deleteCounter(counterId: Int) {
        viewModelScope.launch {
            repository.deleteCounterById(counterId)
        }
    }


    fun deleteRecitation(entry: RecitationEntity) {
        viewModelScope.launch {
            repository.deleteRecitation(entry)
        }
    }

    fun deleteAllForCounter(counterId: Int) {
        viewModelScope.launch {
            repository.deleteByCounterId(counterId)
        }
    }
}

