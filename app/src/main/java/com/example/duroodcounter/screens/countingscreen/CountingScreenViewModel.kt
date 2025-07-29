package com.example.duroodcounter.screens.countingscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duroodcounter.data.counteritemdata.RecitationEntity
import com.example.duroodcounter.repositary.RecitationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountingScreenViewModel @Inject constructor(
    private val repository: RecitationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _liveCount = savedStateHandle.getStateFlow("liveCount", 0)
    val liveCount: StateFlow<Int> = savedStateHandle.getStateFlow("liveCount", 0)

    private var recitationId: Int?
        get() = savedStateHandle["recitationId"]
        set(value) {
            savedStateHandle["recitationId"] = value
        }
    private var currentCounterId: Int? = null
    private val _showTargetDialog = MutableStateFlow(false)
    val showTargetDialog: StateFlow<Boolean> = _showTargetDialog

    private var target: Int = 0

    fun startCounting(counterId: Int, counterTarget: Int) {
        currentCounterId = counterId
        target = counterTarget

        // Only initialize if it's not already set
        if (!savedStateHandle.contains("liveCount")) {
            savedStateHandle["liveCount"] = 0
        }

        if (!savedStateHandle.contains("recitationId")) {
            recitationId = null
        }
    }


    fun incrementCount() {
        val newCount = _liveCount.value + 1
        savedStateHandle["liveCount"] = newCount
        saveIfNeeded()

        if (newCount == target) {
            _showTargetDialog.value = true
        }
    }


    fun dismissDialog() {
        _showTargetDialog.value = false
    }
    private fun saveIfNeeded() {
        val count = _liveCount.value
        val counterId = currentCounterId ?: return

        if (count == 0) {
            // No need to store if zero
            return
        }

        viewModelScope.launch {
            if (recitationId == null) {
                val newId = repository.insertRecitation(
                    RecitationEntity(counterId = counterId, amount = count, timestamp = System.currentTimeMillis())
                )
                recitationId = newId.toInt()
            } else {
                repository.updateRecitationAmount(recitationId!!, count)
            }
        }
    }
}

