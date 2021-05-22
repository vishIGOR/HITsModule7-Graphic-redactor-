package com.example.iso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit


class FiltersFragment : Fragment(R.layout.fragment_filters) {

    var saveNegativeButton: ImageView? = null
    var goToPixButton: ImageView? = null
    var goToColoringButton: ImageView? = null
    var goToBlurButton: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_filters, container, false)
        saveNegativeButton = rootView.findViewById(R.id.negative)
        saveNegativeButton?.setOnClickListener { goToNegative() }

        goToPixButton = rootView.findViewById(R.id.pix)
        goToPixButton?.setOnClickListener { goToPix() }

        goToColoringButton = rootView.findViewById(R.id.colors)
        goToColoringButton?.setOnClickListener { goToColoring() }

        //goToBlurButton = rootView.findViewById(R.id.pix)
        //goToBlurButton?.setOnClickListener { goToBlur() }

        returnButton = rootView.findViewById(R.id.returnToSecondPageButtonFilters)
        returnButton?.setOnClickListener{
            returnToMainMenu()
        }
        return rootView
    }

    private fun returnToMainMenu() {
        val returnFragment: Fragment = MainMenuFragment()
        val transForMenu: FragmentManager = this.fragmentManager!!

        transForMenu.commit {
            replace(R.id.fragments, returnFragment)
        }
    }

    private fun goToNegative() {
        val negativeFragment: Fragment = NegativeFilterFragment()
        val transForNegative: FragmentManager = this.fragmentManager!!

        transForNegative.commit {
            replace(R.id.fragments, negativeFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun goToPix() {
        val pixFragment: Fragment = PixelationFilterFragment()
        val transForPix: FragmentManager = this.fragmentManager!!

        transForPix.commit {
            replace(R.id.fragments, pixFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun goToColoring() {
        val coloringFragment: Fragment = ColoringFilterFragment()
        val transForColoring: FragmentManager = this.fragmentManager!!

        transForColoring.commit {
            replace(R.id.fragments, coloringFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun goToBlur() {
        val blurFragment: Fragment = BlurFilterFragment()
        val transForBlur: FragmentManager = this.fragmentManager!!

        transForBlur.commit {
            replace(R.id.fragments, blurFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }
}