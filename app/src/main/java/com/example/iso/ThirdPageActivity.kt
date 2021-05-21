package com.example.iso

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.annotation.IdRes
import com.example.iso.databinding.ChoiceOfAlgoPageBinding
import kotlinx.android.synthetic.main.choice_of_algo_page.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import java.io.IOException
import by.kirich1409.viewbindingdelegate.viewBinding

class ThirdPageActivity : AppCompatActivity() {

    private val viewBinding by viewBinding(ChoiceOfAlgoPageBinding::bind, R.id.constL)
    lateinit var myUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choice_of_algo_page)

        val extras = intent.extras
        myUri = Uri.parse(extras!!.getString("imageUri"))
        viewBinding.placeForImageSelectionPage.setImageURI(myUri)

        fun fromThirdToSecond() {
            val thirdToSecond = Intent(this@ThirdPageActivity, SecondPageActivity::class.java)
            startActivity(thirdToSecond)
        }

        viewBinding.returnToSecondPageButton.setOnClickListener { fromThirdToSecond() }

        fun fromThirdToFinal() {
            val thirdToFinal = Intent(this@ThirdPageActivity, FinalPageActivity::class.java)
            startActivity(thirdToFinal)
        }

        viewBinding.endWorkingWithImageButton.setOnClickListener { fromThirdToFinal() }

        fun fromThirdToRotation() {
            NavigationItemsRouter(R.id.fragments, supportFragmentManager)
        }
        viewBinding.imageRotationButton.setOnClickListener { fromThirdToRotation() }
    }

    fun fromUriToBitmap(): Bitmap {
        lateinit var bitmapPicture: Bitmap
        try {
            bitmapPicture = MediaStore.Images.Media.getBitmap(this.contentResolver, myUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmapPicture
    }

    class NavigationItemsRouter(
        @IdRes private val containerId: Int,
        private val fm: FragmentManager
    ) {
        fun selectItem(
            screenTag: String,
            screenCreator: () -> Fragment
        ) {
            val activeFragment = findActiveFragment()
            val targetFragment = fm.findFragmentByTag(screenTag)

            if (isSame(activeFragment, targetFragment))
                return

            fm.beginTransaction().run {
                if (activeFragment != null) {
                    hide(activeFragment)
                    // pause fragment
                    setMaxLifecycle(activeFragment, Lifecycle.State.STARTED)
                }

                if (targetFragment == null) {
                    add(containerId, screenCreator(), screenTag)
                } else {
                    // allow resume
                    setMaxLifecycle(targetFragment, Lifecycle.State.RESUMED)
                    show(targetFragment)
                }

                commitAllowingStateLoss()
            }
        }

        /**
         * @return First visible [Fragment].
         */
        private fun findActiveFragment(): Fragment? {
            fm.fragments.forEach {
                if (it.isVisible)
                    return it
            }

            return null
        }

        private fun isSame(active: Fragment?, target: Fragment?): Boolean =
            active != null && target != null && active == target
    }
}
