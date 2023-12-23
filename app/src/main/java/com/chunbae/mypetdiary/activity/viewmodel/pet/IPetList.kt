package com.chunbae.mypetdiary.activity.viewmodel.pet

import com.chunbae.mypetdiary.db.domain.pet.Pet

interface IPetList {
    fun onDataChanged(petList: ArrayList<Pet>)
}