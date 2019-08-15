package com.tfm.equip.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.tfm.equip.AsyncTasks.GetAPIPlacesEquipmentsAndSaveBD
import com.tfm.equip.AsyncTasks.LogoutUserBD
import com.tfm.equip.R
import com.tfm.equip.Utils.CallbackInterface
import com.tfm.equip.Utils.SharedData

class PrincipalActivity: AppCompatActivity() {
    lateinit var placesBtn:Button
    lateinit var stateBtn:Button
    lateinit var loadingProgressBar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        placesBtn = findViewById(R.id.allowedPlacesBtn)
        stateBtn = findViewById(R.id.stateBtn)
        loadingProgressBar = findViewById(R.id.loadingLayoutPrincipal)

        val loggedApi:Boolean = intent.getBooleanExtra("loginApi", false)
        manageLoggedApi(loggedApi)


        placesBtn.setOnClickListener {
            var intent:Intent = Intent(this, PlacesActivity::class.java)
            startActivity(intent)
        }

        stateBtn.setOnClickListener {
            var intent:Intent = Intent(this, StateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_actions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.action_logout){
            LogoutUserBD(this).execute(null)
            var intent:Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else if(item?.itemId == R.id.action_update){
            manageLoggedApi(true)
        }

        return super.onOptionsItemSelected(item)
    }

    fun manageLoggedApi(loggedApi:Boolean){
        if(!loggedApi){
            return
        }

        //Desabilitamos elementos visuales
        placesBtn.isEnabled = false
        placesBtn.isEnabled = false

        placesBtn.alpha = 0.5f
        stateBtn.alpha = 0.5f
        loadingProgressBar.visibility = View.VISIBLE

        GetAPIPlacesEquipmentsAndSaveBD(this, object: CallbackInterface<Boolean>{
            override fun doCallback(obj: Boolean) {
                placesBtn.isEnabled = true
                placesBtn.isEnabled = true

                placesBtn.alpha = 1f
                stateBtn.alpha = 1f
                loadingProgressBar.visibility = View.GONE
            }
        }).execute(SharedData.User)
    }
}