package com.chunbae.mypetdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chunbae.mypetdiary.activity.pet.fragment.PetListFragment

class MainHomeFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_main_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petListFgr = PetListFragment()
        addChildrenFragment(petListFgr)

        setCalendar()

    }
    //MainHomeFragment에서는 petList를 보여준다.
    private fun addChildrenFragment(fragment: PetListFragment){
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frg_petlist, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setCalendar(){

    }

}