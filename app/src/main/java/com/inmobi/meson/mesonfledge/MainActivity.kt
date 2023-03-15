package com.inmobi.meson.mesonfledge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.inmobi.inmobiads.InMobiSdk2
import com.inmobi.meson.mesonsdk.MesonSdk
import com.inmobi.meson.mesonsdk.banner.MesonBanner
import com.inmobi.meson.mesonsdk.listeners.MesonInitListener
import com.inmobi.sdk.InMobiSdk
import com.inmobi.sdk.SdkInitializationListener
import org.json.JSONObject
import java.lang.Error

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.join).setOnClickListener {
            if (MesonSdk.isInitialised()) {
                InMobiSdk.init(this@MainActivity, "df19afdaf27f4fb4a2c2b85e2c10bc6a", JSONObject(), object : SdkInitializationListener{
                    override fun onInitializationComplete(error: Error?) {
                        error?.let {
                            Log.e("MainActivity", "inmobi init failed", it)
                        } ?: kotlin.run {
                            Log.i("MainActivity", "inmobi init success. adding remarketing data")
                            InMobiSdk2.targetForRemarketing(this@MainActivity, "12345")
                        }
                    }

                })
            } else {
                Log.e("MainActivity", "not init")
            }
        }
        findViewById<Button>(R.id.select).setOnClickListener {
            if (MesonSdk.isInitialised()){
                MesonBanner(this@MainActivity, "3245334").load()
            } else {
                Log.e("MainActivity", "not init")
            }
        }
        MesonSdk.init(this, "ghffjhffkgkj", object : MesonInitListener{
            override fun onComplete(error: Error?) {
                error?.let {
                    Log.e("MainActivity", "init fail", it)
                } ?: kotlin.run {
                    Log.i("MainActivity", "init success")
                }

            }

        })
    }
}