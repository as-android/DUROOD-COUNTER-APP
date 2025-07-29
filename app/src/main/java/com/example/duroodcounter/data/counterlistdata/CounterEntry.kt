package com.example.duroodcounter.data.counterlistdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counters")
data class CounterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val target: Int,
    val recited: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
