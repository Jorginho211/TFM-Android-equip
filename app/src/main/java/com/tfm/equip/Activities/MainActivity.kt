package com.tfm.equip.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tfm.equip.DTOs.UserDTO
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.AsyncTasks.GetLastLoggedUserBD
import com.tfm.equip.AsyncTasks.InsertOrUpdateUserBD
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.R
import com.tfm.equip.Services.EquipmentService
import com.tfm.equip.Utils.CallbackInterface
import com.tfm.equip.Utils.Constants
import com.tfm.equip.Utils.RestApi
import com.tfm.equip.Utils.SharedData
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(supportActionBar !== null){
            supportActionBar?.hide()
        }

        val usernameTextView: EditText = findViewById(R.id.usernameText)
        val passwordTextView: EditText = findViewById(R.id.passwordText)

        var btnLogin: Button = findViewById(R.id.loginBtn)
        btnLogin.setOnClickListener {
            RestApi.getInstance().login(usernameTextView.text.toString(), passwordTextView.text.toString(), object: CallbackInterface<UserDTO?>{
                override fun doCallback(userDto: UserDTO?) {
                    if(userDto === null){
                        var containerErrorLogin:ConstraintLayout = findViewById(R.id.containerErrorLogin)
                        this@MainActivity.runOnUiThread {
                            containerErrorLogin.visibility = View.VISIBLE
                        }
                        return
                    }

                    InsertOrUpdateUserBD(
                        this@MainActivity,
                        object : CallbackInterface<UserEntity> {
                            override fun doCallback(userEntity: UserEntity) {
                                SharedData.User = userEntity
                                loginSucess(true)
                            }
                        }).execute(userDto)
                }
            })
        }

        //checkPermissionAndRequest()
    }

    fun loginSucess(loginApi: Boolean){
        var intent:Intent = Intent(this, PrincipalActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("loginApi", loginApi)
        startActivity(intent)
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
