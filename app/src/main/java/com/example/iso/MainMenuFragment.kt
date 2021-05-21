package com.example.iso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.iso.databinding.ChoiceOfAlgoPageBinding
import kotlinx.android.synthetic.main.fragment_main_menu.*


class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {

    var imageRotationButton: ImageView? = null
    var maskingButton: ImageView? = null
    var cubeButton: ImageView? = null
    private lateinit var photoPlace: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)

        val photo = (context as ThirdPageActivity).fromUriToBitmap()
        photoPlace = rootView.findViewById(R.id.placeForImageSelectionPage)
        photoPlace.setImageBitmap(photo)

        imageRotationButton = rootView.findViewById(R.id.imageRotationButton)
        imageRotationButton!!.setOnClickListener { goToRotation() }

        maskingButton = rootView.findViewById(R.id.maskingButton)
        maskingButton!!.setOnClickListener { goToMasking() }

        cubeButton = rootView.findViewById(R.id.cubeButton)
        cubeButton!!.setOnClickListener { goToCube() }

        return rootView
    }

    private fun goToRotation() {
        val rotationFragment: Fragment = ImageRotationFragment()
        val transForRotation: FragmentManager = this.fragmentManager!!

        transForRotation.commit {
            replace(R.id.fragments, rotationFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun goToMasking() {
        val maskingFragment: Fragment = UnsharpMaskingFragment()
        val transForMasking: FragmentManager = this.fragmentManager!!

        transForMasking.commit {
            replace(R.id.fragments, maskingFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun goToCube() {
        val cubeFragment: Fragment = CubeFragment()
        val transForCube: FragmentManager = this.fragmentManager!!

        transForCube.commit {
            replace(R.id.fragments, cubeFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }
}