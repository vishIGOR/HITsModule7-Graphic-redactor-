package com.example.iso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ImageRotationFragment : Fragment(R.layout.fragment_image_rotation) {
    private lateinit var photoPlace: ImageView

    companion object {
        val screenTag: String = ImageRotationFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_rotation, container, false)
        photoPlace = rootView.findViewById(R.id.placeForImageRotation)
        val photo = (context as ThirdPageActivity).fromUriToBitmap()
        photoPlace.setImageBitmap(photo)
        return rootView
    }
}