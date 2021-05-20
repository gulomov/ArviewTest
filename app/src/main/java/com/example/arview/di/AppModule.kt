package com.example.arview.di

import androidx.room.Room
import com.example.arview.data.AppDatabase
import com.example.arview.repository.Repository
import org.koin.dsl.module

val appModule = module {
    single { Repository(errorConverter = get(), api = get(), db = get(), context = get()) }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "arveiw_app")
            .fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().gameDao() }
}