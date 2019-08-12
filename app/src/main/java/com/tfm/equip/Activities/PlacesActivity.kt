package com.tfm.equip.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.tfm.equip.R

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        var list:ArrayList<String> = ArrayList()
        list.add("TEst1")
        list.add("Test2")

        var placesListView:ListView = findViewById(R.id.placesListView)
        var arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        placesListView.adapter = arrayAdapter

        placesListView.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, i, l ->
            Log.i("Selected", list[i])
            var intent:Intent = Intent(this, PlaceActivity::class.java)
            intent.putExtra("place", list[i])
            startActivity(intent)
        }
    }
}
