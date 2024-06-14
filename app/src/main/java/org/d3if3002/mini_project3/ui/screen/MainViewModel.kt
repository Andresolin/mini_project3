package org.d3if3002.mini_project3.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.d3if3002.mini_project3.model.City
import org.d3if3002.mini_project3.network.ApiStatus
import org.d3if3002.mini_project3.network.CityApi

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<City>())
        private set


    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrievedata()
    }

    private fun retrievedata() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = CityApi.service.getCity()
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MianViewModel", "Failure: ${e.message}")
            }
        }
    }
}