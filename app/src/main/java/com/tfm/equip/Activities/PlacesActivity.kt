package com.tfm.equip.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.tfm.equip.AsyncTasks.GetPlacesBD
import com.tfm.equip.Database.Entities.PlaceEntity
import com.tfm.equip.R
import com.tfm.equip.Utils.CallbackInterface

class PlacesActivity : AppCompatActivity() {
    private lateinit var placesListView: ListView
    private lateinit var placesEntities: ArrayList<PlaceEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        placesListView = findViewById(R.id.placesListView)


        GetPlacesBD(this, object: CallbackInterface<ArrayList<PlaceEntity>>{
            override fun doCallback(places: ArrayList<PlaceEntity>) {
                placesEntities = places
                var arrayAdapter: ArrayAdapter<PlaceEntity> = ArrayAdapter(this@PlacesActivity, android.R.layout.simple_list_item_1, placesEntities)
                placesListView.adapter = arrayAdapter
            }
        }).execute()

        placesListView.onItemClickListener = AdapterView.OnItemClickListener{
            _, _, i, _ ->
            var intent:Intent = Intent(this, PlaceActivity::class.java)
            intent.putExtra("idPlace", placesEntities[i].id)
            intent.putExtra("place", placesEntities[i].Name)
            startActivity(intent)
        }
    }
}
