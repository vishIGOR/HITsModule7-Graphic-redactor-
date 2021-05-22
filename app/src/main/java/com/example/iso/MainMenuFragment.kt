package com.example.iso

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {

    var imageRotationButton: ImageView? = null
    var updateButton: ImageView? = null
    var maskingButton: ImageView? = null
    var cubeButton: ImageView? = null
    var scaleButton: ImageView? = null
    var filtersButton: ImageView? = null
    var endWorkingWithImageButton: ImageView? = null
    private lateinit var photoPlace: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)

        imageRotationButton = rootView.findViewById(R.id.imageRotationButton)
        imageRotationButton!!.setOnClickListener { goToRotation() }

        maskingButton = rootView.findViewById(R.id.maskingButton)
        maskingButton!!.setOnClickListener { goToMasking() }

        cubeButton = rootView.findViewById(R.id.cubeButton)
        cubeButton!!.setOnClickListener { goToCube() }

        scaleButton = rootView.findViewById(R.id.scaleButton)
        scaleButton!!.setOnClickListener { goToScale() }

        filtersButton = rootView.findViewById(R.id.filtersButton)
        filtersButton!!.setOnClickListener { goToFilters() }

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

    private fun goToScale() {
        val scaleFragment: Fragment = ScaleFragment()
        val transForScale: FragmentManager = this.fragmentManager!!

        transForScale.commit {
            replace(R.id.fragments, scaleFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun goToFilters() {
        val filtersFragment: Fragment = FiltersFragment()
        val transForScale: FragmentManager = this.fragmentManager!!

        transForScale.commit {
            replace(R.id.fragments, filtersFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }
}