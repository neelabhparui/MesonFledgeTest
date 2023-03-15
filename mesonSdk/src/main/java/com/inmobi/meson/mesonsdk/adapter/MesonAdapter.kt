package com.inmobi.meson.mesonsdk.adapter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import org.json.JSONObject

interface MesonAdapter {
    fun init(activity: Activity, accountId : String, consentObject : JSONObject, completionListener: OnCompletionListener)
    fun loadInterstitial(context : Context, plcId : String, listener: AdLoadListener)
    fun loadInterstitial(context : Context, plcId : String, response : ByteArray, listener: AdLoadListener)
    fun loadBanner(context : Context, plcId : String, listener: AdLoadListener) : View
    fun loadBanner(context : Context, plcId : String, response : ByteArray, listener: AdLoadListener) : View
    fun destroy()

    interface OnCompletionListener {
        fun onComplete(error: java.lang.Error?)
    }

    interface AdLoadListener {
        fun loaded()
        fun shown()
        fun showFail()
        fun loadFail()
        fun dismissed()
        fun impressed()
    }
}