package com.example.duroodcounter.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duroodcounter.data.counterlistdata.CounterEntity
import com.example.duroodcounter.repositary.CounterRepository
import com.example.duroodcounter.repositary.RecitationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val counterRepository: CounterRepository,
    private val recitationRepository: RecitationRepository
) : ViewModel() {

    data class CounterWithProgress(
        val counter: CounterEntity,
        val totalRecited: Int
    )

    val counterList = counterRepository.counters

    val counterWithProgress: StateFlow<List<CounterWithProgress>> =
        combine(counterList, recitationRepository.getRecitationSums()) { counters, recitations ->
            counters.map { counter ->
                val recited = recitations.find { it.counterId == counter.id }?.totalRecited ?: 0
                CounterWithProgress(counter, recited)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCounter(title: String, target: Int) {
        viewModelScope.launch {
            val counter = CounterEntity(
                title = title,
                target = target,
                createdAt = System.currentTimeMillis()
            )
            counterRepository.addCounter(counter)
        }
    }

    fun deleteCounter(counter: CounterEntity) {
        viewModelScope.launch {
            counterRepository.deleteCounter(counter)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            counterRepository.deleteAllCounters()
        }
    }
}


