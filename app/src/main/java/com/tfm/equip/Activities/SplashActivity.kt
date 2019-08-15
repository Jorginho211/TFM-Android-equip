package com.tfm.equip.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tfm.equip.Database.AsyncTasks.GetLastLoggedUserBD
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.Utils.CallbackInterface

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))

        GetLastLoggedUserBD(this, object : CallbackInterface<UserEntity?> {
            override fun doCallback(userEntity: UserEntity?) {
                if(userEntity !== null){
                    loginSucess(false)
                }
            }
        }).execute()
    }

    fun loginSucess(loginApi: Boolean){
        var intent:Intent = Intent(this, PrincipalActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("loginApi", loginApi)
        startActivity(intent)
    }
}
