package com.inmobi.meson.mesonsdk.banner

import android.adservices.adselection.AdSelectionOutcome
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.RelativeLayout
import com.inmobi.meson.mesonsdk.fledge.FledgeManager
import com.inmobi.meson.mesonsdk.mediation.MediationManager
import java.lang.Exception

@SuppressLint("ViewConstructor")
class MesonBanner(private val activity: Activity, private val plcId : String) : RelativeLayout(activity) {

    fun load() {
        loadAd()
    }

    //On fledge selection complete
//    override fun onComplete(outcome: AdSelectionOutcome?, error: Exception?) {
//        error?.let {
//            Log.e("BANNER", "cannot select ads from fledge", it)
//            //fetch mediation chain (includes bidding + waterfall) with placement id and load
//            MediationManager.loadBanner(activity, this@MesonBanner, plcId)
//        } ?: kotlin.run {
//            outcome?.let {
//                Log.i("BANNER", "loading from fledge")
//                //load remarketing ad
//                MediationManager.loadFromFledgeOutcome(activity, this@MesonBanner, it)
//            } ?: kotlin.run {
//                Log.e("BANNER", "cannot select ads from fledge - no outcome")
//
//                //fetch mediation chain (includes bidding + waterfall) with placement id and load
//                MediationManager.loadBanner(activity, this@MesonBanner, plcId)
//            }
//        }
//    }

    private fun loadAd() {
        MediationManager.loadBanner(activity, this, plcId)
    }
}