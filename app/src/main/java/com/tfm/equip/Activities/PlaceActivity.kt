package com.tfm.equip.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.tfm.equip.AsyncTasks.GetPlaceEquipmentsBD
import com.tfm.equip.DTOs.PlaceDTO
import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Fragments.BlueprintFragment
import com.tfm.equip.Fragments.EquipmentFragment
import com.tfm.equip.R
import com.tfm.equip.Utils.CallbackInterface
import com.tfm.equip.Utils.RestApi
import com.tfm.equip.Utils.SharedData


class PlaceActivity : AppCompatActivity() {
    private lateinit var equipmentFragment: EquipmentFragment
    private lateinit var blueprintFragment: BlueprintFragment
    private lateinit var placeListViewFrame: FrameLayout
    private lateinit var placeImageViewFrame: FrameLayout
    private lateinit var placeEquipmentTextView: TextView
    private lateinit var loadingLayoutPlace: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        supportActionBar?.title = intent.extras.getString("place", "place")
        val idPlace:Int = intent.extras.getInt("idPlace", -1)

        equipmentFragment = EquipmentFragment()
        blueprintFragment = BlueprintFragment()

        placeListViewFrame = findViewById(R.id.placeListViewFrame)
        placeImageViewFrame = findViewById(R.id.placeImageViewFrame)
        placeEquipmentTextView = findViewById(R.id.placeEquipmentTextView)
        loadingLayoutPlace = findViewById(R.id.loadingLayoutPlace)

        placeListViewFrame.visibility = View.INVISIBLE
        placeImageViewFrame.visibility = View.INVISIBLE
        placeEquipmentTextView.visibility = View.INVISIBLE
        loadingLayoutPlace.visibility = View.VISIBLE

        val restApi:RestApi = RestApi()
        restApi.getPlace(idPlace, SharedData.User!!.Token, object : CallbackInterface<PlaceDTO?> {
            override fun doCallback(place: PlaceDTO?) {
                this@PlaceActivity.runOnUiThread{
                    if(place !== null)
                        blueprintFragment.showBlueprint(place.blueprint)

                    placeListViewFrame.visibility = View.VISIBLE
                    placeImageViewFrame.visibility = View.VISIBLE
                    placeEquipmentTextView.visibility = View.VISIBLE
                    loadingLayoutPlace.visibility = View.GONE
                }
            }
        })

        GetPlaceEquipmentsBD(this, object : CallbackInterface<ArrayList<EquipmentEntity>> {
            override fun doCallback(equipments: ArrayList<EquipmentEntity>) {
                equipmentFragment.showEquipments(equipments, null)
            }
        }).execute(idPlace)

        loadFragment(R.id.placeListViewFrame, equipmentFragment)
        loadFragment(R.id.placeImageViewFrame, blueprintFragment)
    }

    private fun loadFragment(id:Int, fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(id, fragment)
            .commit()
        return true
    }
}
