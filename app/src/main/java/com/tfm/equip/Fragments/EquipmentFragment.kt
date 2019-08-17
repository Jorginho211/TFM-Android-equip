package com.tfm.equip.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.tfm.equip.Database.Entities.EquipmentEntity

import com.tfm.equip.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EquipmentFragment: Fragment() {
    lateinit var equipmentListView:ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_equipment, container, false)
        equipmentListView = root.findViewById(R.id.equipmentListView)

        return root
    }

    fun showEquipments(equipments: ArrayList<EquipmentEntity>){
        var adapter = EquipmentsArrayAdapter(this.context!!, R.layout.equipment_listview_item, equipments)
        equipmentListView.adapter = adapter
    }
}
