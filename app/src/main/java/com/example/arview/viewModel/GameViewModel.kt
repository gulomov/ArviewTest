package com.example.arview.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.*
import com.example.arview.network.Resource
import com.example.arview.network.dto.GamesResponse
import com.example.arview.repository.Repository
import com.example.arview.utils.Event
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameViewModel(private val repository: Repository) : ViewModel() {

    val resourceGame = MutableLiveData<Event<Resource<GamesResponse>>>()

    fun getGames() {
        viewModelScope.launch {
            repository.getTopGames().onEach {
                resourceGame.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

}