package com.tfm.equip.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.tfm.equip.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EquipmentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var root = inflater.inflate(R.layout.fragment_equipment, container, false)

        var list = ArrayList<String>()

        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")
        list.add("Equip1")

        var equipmentListView:ListView = root.findViewById(R.id.equipmentListView)
        var arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, list)
        equipmentListView.adapter = arrayAdapter

        return root
    }
}
