package com.chunbae.mypetdiary.db.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chunbae.mypetdiary.db.dao.GuardianDao
import com.chunbae.mypetdiary.db.domain.user.Guardian
import com.chunbae.mypetdiary.db.repository.GuardianRepository
import kotlinx.coroutines.launch

class GuardianViewModel(
    private val userRepo: GuardianRepository
) : ViewModel() {
    private val userList = MutableLiveData<List<Guardian>>()
    val guardian: LiveData<List<Guardian>> get() = userList

     fun getUserInfo() {
        viewModelScope.launch {
           var guardian = userRepo.getGuardianInfo()
            guardian.forEach { Log.d("MyPetDiaryLogs", it.toString()) }
            userList.value = guardian
       }
    }

}

class GuardianViewModelFactory(private val userRepo: GuardianRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GuardianViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return GuardianViewModel(userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}