package com.tfm.equip.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tfm.equip.Fragments.BlueprintFragment
import com.tfm.equip.Fragments.EquipmentFragment
import com.tfm.equip.R


class PlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        supportActionBar?.title = intent?.extras?.getString("place", "place")

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
