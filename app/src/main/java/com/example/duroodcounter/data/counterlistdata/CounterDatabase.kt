package com.example.duroodcounter.data.counterlistdata

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.duroodcounter.data.counteritemdata.RecitationDao
import com.example.duroodcounter.data.counteritemdata.RecitationEntity

@Database(
    entities = [CounterEntity::class, RecitationEntity::class],
    version = 1
)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
    abstract fun recitationDao(): RecitationDao
}

