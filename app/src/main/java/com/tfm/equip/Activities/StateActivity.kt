package com.tfm.equip.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.widget.ImageView
import com.tfm.equip.Fragments.BlueprintFragment
import com.tfm.equip.Fragments.EquipmentFragment
import com.tfm.equip.R

class StateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        supportActionBar?.title = resources.getString(R.string.state_title)
        loadFragment(R.id.stateImageViewFrame, BlueprintFragment())
        loadFragment(R.id.stateListViewFrame, EquipmentFragment())
    }

    private fun loadFragment(id:Int, fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(id, fragment)
            .commit()
        return true
    }
}
