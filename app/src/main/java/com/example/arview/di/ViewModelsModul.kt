package com.example.arview.di

import com.example.arview.viewModel.DbViewModel
import com.example.arview.viewModel.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { DbViewModel(get()) }
}