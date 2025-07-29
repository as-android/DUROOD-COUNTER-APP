package com.example.duroodcounter.data.counteritemdata


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recitations")
data class RecitationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val counterId: Int,        // Foreign key reference to CounterEntity
    val amount: Int,           // How many recited
    val timestamp: Long = System.currentTimeMillis() // When it happened
)
