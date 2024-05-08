package com.chunbae.mypetdiary.activity.pet.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.activity.pet.PetListMainActivity
import com.chunbae.mypetdiary.activity.viewmodel.pet.VMPetList
import com.chunbae.mypetdiary.common.DpToPixelConverter
import com.chunbae.mypetdiary.common.ImageTypeConverter
import com.chunbae.mypetdiary.common.code.CommonStringCode
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.google.android.flexbox.FlexboxLayout
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class PetListFragment : Fragment() {

    private lateinit var imageTypeConverter: ImageTypeConverter
    private var eventListener: FragmentEventListener? = null
    private lateinit var vmPetList: VMPetList
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_pet_list_fragment, container, false)
        imageTypeConverter = ImageTypeConverter()
        vmPetList = ViewModelProvider(requireActivity())[VMPetList::class.java]

        settingPetList(ArrayList<Pet>())

        vmPetList.data.observe(viewLifecycleOwner, Observer{ it ->
            settingPetList(it)
        })

        return root
    }
    
    //Activity에서 Fragment를 호출 할 때 실행되는 메서드
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentEventListener) {
            eventListener = context
        }else {
            throw ClassCastException("$context must implement OnButtonClickListener")
        }
    }

    fun settingPetList(petList: ArrayList<Pet>) {
        var flexboxLayout = root.findViewById<FlexboxLayout>(R.id.flex_layout)!!
        flexboxLayout.removeAllViews()

        if (petList.isNotEmpty()) {
            var receivedData: Long = 0L
            if(arguments?.getString("petId") != null){
                receivedData = arguments?.getLong("petId")!!
            }
            for ((idx, pet) in petList.withIndex()) {
                var btn = makeCircleImageButton(pet)
                btn.setOnClickListener { selectPet(pet.id) }
                flexboxLayout.addView(btn, idx)
                //선택한 petId로 선택 표시해줌
                if(receivedData != null && pet.id == receivedData){
                    btn.borderColor = ContextCompat.getColor(requireContext(), R.color.black)
                    btn.borderWidth = 10
                }
            }
        }
        if(context is PetListMainActivity) {
            flexboxLayout.addView(makeAddBtn())
        }
    }

    private fun makeAddBtn(): CircleImageView {
        var circleButton: CircleImageView = CircleImageView(requireContext())

        // dp를 픽셀로 변환
        var pixcels = DpToPixelConverter().convert(resources, 70)
        var mPixcel = DpToPixelConverter().convert(resources, 10)

        // 레이아웃 파라미터가 null이면 새로 생성
        val newLayoutParams: FlexboxLayout.LayoutParams =
            FlexboxLayout.LayoutParams(pixcels, pixcels)

        newLayoutParams.width = pixcels
        newLayoutParams.height = pixcels
        newLayoutParams.setMargins(mPixcel, mPixcel, mPixcel, mPixcel)

        circleButton.setImageResource(R.drawable.plus_btn)
        circleButton.setOnClickListener { addPet() }
        circleButton.id = R.id.plusBtn
        circleButton.scaleType = ImageView.ScaleType.CENTER_CROP
        circleButton.layoutParams = newLayoutParams

        return circleButton

    }

    fun makeCircleImageButton(pet: Pet): CircleImageView {
        var circleButton: CircleImageView = CircleImageView(requireContext())
        var bitmap: Bitmap = imageTypeConverter.toBitmap(pet.image)

        // dp를 픽셀로 변환
        var pixcels = DpToPixelConverter().convert(resources, 70)
        var mPixcel = DpToPixelConverter().convert(resources, 10)

        // 레이아웃 파라미터가 null이면 새로 생성
        val newLayoutParams: FlexboxLayout.LayoutParams =
            FlexboxLayout.LayoutParams(pixcels, pixcels)

        newLayoutParams.width = pixcels
        newLayoutParams.height = pixcels
        newLayoutParams.setMargins(mPixcel, mPixcel, mPixcel, mPixcel)

        circleButton.setImageBitmap(bitmap)
        circleButton.scaleType = ImageView.ScaleType.CENTER_CROP
        circleButton.layoutParams = newLayoutParams
        circleButton.borderColor = ContextCompat.getColor(requireContext(), R.color.gray)
        circleButton.borderWidth = 5

        circleButton.setOnClickListener { pet.id?.let { it1 -> selectPet(it1) } }
        return circleButton
    }

    /*
    * activity와 통신*/
    private fun callActivityMethod(text: String, obj: Any?){
        eventListener?.onFragmentEvent(text, obj)
    }

    //펫 메인 화면으로 이동
    private fun selectPet(id: Long?) {
        callActivityMethod(CommonStringCode.SEL_PET.value, id)
    }

    //펫 등록화면으로 이동
    private fun addPet() {
        callActivityMethod(CommonStringCode.ADD_PET.value, null)
    }

    fun Parcelable.toUUID(): UUID {
        return UUID.fromString((this as Parcel).readString())
    }


}