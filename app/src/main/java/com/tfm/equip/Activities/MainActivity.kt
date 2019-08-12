package com.tfm.equip.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tfm.equip.R
import com.tfm.equip.Services.EquipmentService
import com.tfm.equip.Utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(supportActionBar !== null){
            supportActionBar?.hide()
        }

        //checkPermissionAndRequest()

        var btnLogin: Button = findViewById(R.id.loginBtn)
        btnLogin.setOnClickListener {
            var intent:Intent = Intent(this, PrincipalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }

    fun checkPermissionAndRequest(){
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }
        else {
            var intent:Intent = Intent(this, EquipmentService::class.java)
            intent.putExtra("workerUUID", Constants.UUID_WORKER)
            startService(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    var intent:Intent = Intent(this, EquipmentService::class.java)
                    intent.putExtra("workerUUID", Constants.UUID_WORKER)
                    startService(intent)
                }
            }
        }
    }
}
