package com.example.pocketplant.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pocketplant.model.Plant

class PlantViewModel : ViewModel() {

    private val _plants = MutableLiveData<List<Plant>>(emptyList())
    val plants: LiveData<List<Plant>> = _plants

    /** ✅ Add new plant */
    fun addPlant(plant: Plant) {
        val updatedList = _plants.value.orEmpty() + plant
        _plants.value = updatedList
    }

    /** ✅ Update existing plant */
    fun updatePlant(updated: Plant) {
        val currentList = _plants.value.orEmpty()
        val updatedList = currentList.map { if (it.id == updated.id) updated else it }
        _plants.value = updatedList
    }

    /** ✅ Get LiveData for specific plant ID */
    fun getPlantById(id: Int): LiveData<Plant?> {
        val result = MutableLiveData<Plant?>()
        result.value = _plants.value?.find { it.id == id }

        // Automatically update when plants list changes
        _plants.observeForever { list ->
            result.value = list.find { it.id == id }
        }

        return result
    }
}
