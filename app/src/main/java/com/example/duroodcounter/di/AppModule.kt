package com.example.duroodcounter.di

import android.app.Application
import androidx.room.Room
import com.example.duroodcounter.data.counteritemdata.RecitationDao
import com.example.duroodcounter.data.counterlistdata.CounterDao
import com.example.duroodcounter.data.counterlistdata.CounterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CounterDatabase =
        Room.databaseBuilder(app, CounterDatabase::class.java, "counter_db").build()

    @Provides
    fun provideDao(db: CounterDatabase): CounterDao = db.counterDao()
    @Provides
    fun provideRecitationDao(db: CounterDatabase): RecitationDao = db.recitationDao()

}
