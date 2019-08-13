package com.tfm.equip.Fragments


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.tfm.equip.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BlueprintFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root:View = inflater.inflate(R.layout.fragment_blueprint, container, false)

        var base64String:String = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAABn0lEQVR42u3TAQ0AAAjDMO5fNCAABaTNLCxdtQGXGAQMAgYBg4BBwCBgEDAIGAQMAhgEDAIGAYOAQcAgYBAwCBgEDGIQMAgYBAwCBgGDgEHAIGAQMAhgEDAIGAQMAgYBg4BBwCBgEDAIYBAwCBgEDAIGAYOAQcAgYBDAIGAQMAgYBAwCBgGDgEHAIGAQwCBgEDAIGAQMAgYBg4BBwCCAQcAgYBAwCBgEDAIGAYOAQcAggEHAIGAQMAgYBAwCBgGDgEHAIAYBg4BBwCBgEDAIGAQMAgYBgwAGAYOAQcAgYBAwCBgEDAIGAYMYBAwCBgGDgEHAIGAQMAgYBAwCGAQMAgYBg4BBwCBgEDAIGAQMAhgEDAIGAYOAQcAgYBAwCBgEMAgYBAwCBgGDgEHAIGAQMAgYBDAIGAQMAgYBg4BBwCBgEDAIYBAwCBgEDAIGAYOAQcAgYBAwCGAQMAgYBAwCBgGDgEHAIGAQMIhBwCBgEDAIGAQMAgYBg4BBwCCAQcAgYBAwCBgEDAIGAYOAQcAgBgGDgEHAIGAQMAgYBAwCHw2n4Y9IKdr/IQAAAABJRU5ErkJggg=="
        var base64Image:String = base64String.split(",")[1]

        var decodedString = Base64.decode(base64Image, Base64.DEFAULT)
        var decodedByte:Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        var blueprintImageView:ImageView = root.findViewById(R.id.blueprintImageView)
        blueprintImageView.setImageBitmap(decodedByte)

        return root
    }
}
