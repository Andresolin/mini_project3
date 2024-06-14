package org.d3if3002.mini_project3.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3002.mini_project3.network.CityApi

class MainViewModel : ViewModel() {

    init {
        retrievedata()
    }

    private fun retrievedata() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = CityApi.service.getCity()
                Log.d("MainViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
}