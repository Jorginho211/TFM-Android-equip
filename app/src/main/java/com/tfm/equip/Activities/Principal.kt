package com.tfm.equip.Activities

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tfm.equip.R

class Principal: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_principal)
        if(actionBar !== null){
            actionBar.title = Resources.getSystem().getResourceName(R.string.principal_title)
        }
    }
}