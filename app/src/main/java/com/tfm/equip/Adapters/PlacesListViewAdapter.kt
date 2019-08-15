package com.tfm.equip.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tfm.equip.Database.Entities.PlaceEntity

class PlacesListViewAdapter(context:Context, id: Int, placesEntities: ArrayList<PlaceEntity>): ArrayAdapter<PlaceEntity>(context, id, placesEntities){

}