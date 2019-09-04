package com.tfm.equip.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tfm.equip.DTOs.PlaceDTO
import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Fragments.BlueprintFragment
import com.tfm.equip.Fragments.EquipmentFragment
import com.tfm.equip.R
import com.tfm.equip.Utils.CallbackInterface
import com.tfm.equip.Utils.RestApi
import com.tfm.equip.Utils.SharedData

class StateActivity : AppCompatActivity() {
    private lateinit var equipmentFragment: EquipmentFragment
    private lateinit var blueprintFragment: BlueprintFragment
    private lateinit var stateListViewFrame: FrameLayout
    private lateinit var stateImageViewFrame: FrameLayout
    private lateinit var stateEquipmentTextView: TextView
    private lateinit var loadingLayoutState: LinearLayout
    private lateinit var statePlaceNameTextView: TextView
    private lateinit var statePlaceNoPermisionImageView: ImageView

    private lateinit var equipments:ArrayList<EquipmentEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        supportActionBar?.title = resources.getString(R.string.state_title)

        equipmentFragment = EquipmentFragment()
        blueprintFragment = BlueprintFragment()

        loadFragment(R.id.stateListViewFrame, equipmentFragment)
        loadFragment(R.id.stateImageViewFrame, blueprintFragment)

        statePlaceNameTextView = findViewById(R.id.statePlaceNameTextView)
        statePlaceNoPermisionImageView = findViewById(R.id.statePlaceNoPermisionImageView)
        stateListViewFrame = findViewById(R.id.stateListViewFrame)
        stateImageViewFrame = findViewById(R.id.stateImageViewFrame)
        stateEquipmentTextView = findViewById(R.id.stateEquipmentTextView)
        loadingLayoutState = findViewById(R.id.loadingLayoutState)

        statePlaceNameTextView.visibility = View.INVISIBLE
        statePlaceNoPermisionImageView.visibility = View.INVISIBLE
        stateListViewFrame.visibility = View.INVISIBLE
        stateImageViewFrame.visibility = View.INVISIBLE
        stateEquipmentTextView.visibility = View.INVISIBLE
        loadingLayoutState.visibility = View.VISIBLE


        var hasCallRestApi: Boolean = false
        if(SharedData.PlaceState !== null){
            if(!SharedData.PlaceState!!.HasPermission){
                statePlaceNameTextView.setTextColor(getColor(R.color.colorError))
                statePlaceNameTextView.text = SharedData.PlaceState!!.Name
            }

            hasCallRestApi = true
            val restApi: RestApi = RestApi()
            restApi.getPlace(SharedData.PlaceState!!.id, SharedData.User!!.Token, object : CallbackInterface<PlaceDTO?> {
                override fun doCallback(place: PlaceDTO?) {
                    this@StateActivity.runOnUiThread{
                        if(place !== null)
                            blueprintFragment.showBlueprint(place.blueprint)

                        statePlaceNameTextView.visibility = View.VISIBLE
                        stateListViewFrame.visibility = View.VISIBLE
                        stateImageViewFrame.visibility = View.VISIBLE
                        stateEquipmentTextView.visibility = View.VISIBLE
                        loadingLayoutState.visibility = View.GONE

                        if(!SharedData.PlaceState!!.HasPermission){
                            statePlaceNoPermisionImageView.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
        else {
            statePlaceNameTextView.text = getString(R.string.state_no_area)
        }

        equipments= SharedData.EquipmentsPlaceStateRequired.clone() as ArrayList<EquipmentEntity>
        for (eq in SharedData.EquipmentsState){
            if(equipments.find { e -> e.id === eq.id } === null){
                equipments.add(eq)
            }
        }

        equipments = ArrayList(equipments.sortedWith(Comparator<EquipmentEntity> { a, b ->
            when {
                a.IsEquip === true && a.IsEquip !== b.IsEquip -> 0
                a.IsEquip === false && a.IsEquip !== b.IsEquip -> -1
                else -> 0
            }
        }))

        if(!hasCallRestApi){
            statePlaceNameTextView.visibility = View.VISIBLE
            stateListViewFrame.visibility = View.VISIBLE
            stateImageViewFrame.visibility = View.VISIBLE
            stateEquipmentTextView.visibility = View.VISIBLE
            loadingLayoutState.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()

        equipmentFragment.showEquipments(equipments)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun loadFragment(id:Int, fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(id, fragment)
            .commit()
        return true
    }
}
