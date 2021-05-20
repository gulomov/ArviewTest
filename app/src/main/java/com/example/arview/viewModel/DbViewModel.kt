package com.example.arview.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arview.data.GameInfo
import com.example.arview.repository.Repository
import kotlinx.coroutines.launch

class DbViewModel(private var repository: Repository) : ViewModel() {
    val gameList: LiveData<List<GameInfo>> = repository.getGamesFromDb()
    fun insert(gameInfo: GameInfo) = viewModelScope.launch {
        repository.insert(gameInfo)
    }
}