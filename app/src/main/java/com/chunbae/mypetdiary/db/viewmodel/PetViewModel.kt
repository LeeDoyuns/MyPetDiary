package com.chunbae.mypetdiary.db.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.chunbae.mypetdiary.db.repository.PetRepository
import kotlinx.coroutines.launch

class PetViewModel(
    private val petRepo: PetRepository
) : ViewModel() {

    private var petList = MutableLiveData<List<Pet>>()
    val pet: LiveData<List<Pet>> get() = petList

    fun getPetList(){
        viewModelScope.launch {
            var list: List<Pet> = petRepo.selectPetList()
            list.forEach { Log.d("MyPetDiaryLogs", "pet list => ${list}")
            }
            petList.value = list
        }
    }
}

class PetViewModelFactory(private val petRepo: PetRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PetViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PetViewModel(petRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
