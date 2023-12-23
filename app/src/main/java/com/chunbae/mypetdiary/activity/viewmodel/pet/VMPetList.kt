package com.chunbae.mypetdiary.activity.viewmodel.pet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chunbae.mypetdiary.db.domain.pet.Pet

class VMPetList : ViewModel() {

    private val _data = MutableLiveData<ArrayList<Pet>>()
    val data: LiveData<ArrayList<Pet>>
        get() = _data


    fun setData(petList: ArrayList<Pet>){
        _data.value = petList
    }


}