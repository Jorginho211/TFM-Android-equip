package com.tfm.equip.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tfm.equip.AsyncTasks.GetLastLoggedUserBD
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.Utils.CallbackInterface
import com.tfm.equip.Utils.SharedData
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        certificate()

        GetLastLoggedUserBD(this, object : CallbackInterface<UserEntity?> {
            override fun doCallback(userEntity: UserEntity?) {
                if (userEntity !== null) {
                    SharedData.User = userEntity
                    loginSucess(false)
                }
                else {
                    var intent:Intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }).execute()
    }

    private fun certificate(){
        try {
            val victimizedManager = arrayOf<TrustManager>(

                object : X509TrustManager {
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                    }

                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                })

            val sc = SSLContext.getInstance("SSL")
            sc.init(null, victimizedManager, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
            HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun loginSucess(loginApi: Boolean){
        var intent:Intent = Intent(this, PrincipalActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("loginApi", loginApi)
        intent.putExtra("startEquipmentService", true)
        startActivity(intent)
    }
}
