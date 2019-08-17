package com.tfm.equip.Fragments

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tfm.equip.Database.Entities.EquipmentEntity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.tfm.equip.R


class EquipmentsArrayAdapter(context:Context, resource:Int, equipments:List<EquipmentEntity>): ArrayAdapter<EquipmentEntity>(context, resource, equipments) {
    private val resource:Int
    private val equipments:List<EquipmentEntity>

    init {
        this.resource = resource
        this.equipments = equipments
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(resource, null, false)

        val nameTextView:TextView = view.findViewById(R.id.equipment_listview_item_name)
        val warningImageView:ImageView = view.findViewById(R.id.equipment_listview_item_warning)

        val equipmentEntity: EquipmentEntity = equipments.get(position)

        nameTextView.text = equipmentEntity.Name
        warningImageView.visibility = View.INVISIBLE
        if(!equipmentEntity.IsEquip){
            warningImageView.visibility = View.VISIBLE
            nameTextView.setTextColor(this.context.getColor(R.color.colorError))
        }

        return view
    }
}