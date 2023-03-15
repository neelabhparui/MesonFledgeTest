package com.example.thirdParty

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener
import com.inmobi.meson.mesonsdk.adapter.MesonAdapter
import com.inmobi.sdk.InMobiSdk
import com.inmobi.sdk.SdkInitializationListener
import org.json.JSONObject
import java.lang.Error

class ThirdPartyAdapter : MesonAdapter {

    private var inter : InMobiInterstitial? = null

    override fun init(
        activity: Activity,
        accountId: String,
        consentObject: JSONObject,
        completionListener: MesonAdapter.OnCompletionListener
    ) {
        activity.runOnUiThread {
            InMobiSdk.init(activity, accountId, consentObject, object : SdkInitializationListener {
                override fun onInitializationComplete(error: Error?) {
                    error?.let {
                        Log.e("[InMobi Meson Adapter]", "failed to init adapter", it)
                        completionListener.onComplete(it)
                    } ?: kotlin.run {
                        Log.i("[InMobi Meson Adapter]", "init complete")
                        completionListener.onComplete(null)
                    }
                }

            })
        }
    }

    override fun loadInterstitial(
        context: Context,
        plcId: String,
        listener: MesonAdapter.AdLoadListener
    ) {
        inter = createInterstitial(context, plcId)
        inter?.load()
    }

    override fun loadInterstitial(
        context: Context,
        plcId: String,
        response: ByteArray,
        listener: MesonAdapter.AdLoadListener
    ) {
        inter = createInterstitial(context, plcId)
        inter?.load(response)
    }

    override fun loadBanner(context: Context, plcId: String, listener: MesonAdapter.AdLoadListener): View {
        val banner = InMobiBanner(context, plcId.toLong())
        banner.load()
        return banner
    }

    override fun loadBanner(
        context: Context,
        plcId: String,
        response: ByteArray,
        listener: MesonAdapter.AdLoadListener
    ): View {
        val banner = InMobiBanner(context, plcId.toLong())
        banner.load(response)
        return banner
    }

    override fun destroy() {
        inter = null
    }

    private fun createInterstitial(context: Context, plcId: String) = InMobiInterstitial(context, plcId.toLong(), object : InterstitialAdEventListener(){
        override fun onAdFetchSuccessful(p0: InMobiInterstitial, p1: AdMetaInfo) {
            Log.i("[InMobi Meson Adapter]", "fetch success")
            super.onAdFetchSuccessful(p0, p1)
        }

        override fun onAdLoadSucceeded(p0: InMobiInterstitial, p1: AdMetaInfo) {
            Log.i("[InMobi Meson Adapter]", "load success")
            inter?.show()
            super.onAdLoadSucceeded(p0, p1)
        }

        override fun onAdLoadFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
            Log.e("[InMobi Meson Adapter]", "fetch fail")
            super.onAdLoadFailed(p0, p1)
        }

        override fun onAdClicked(p0: InMobiInterstitial, p1: MutableMap<Any, Any>?) {
            Log.i("[InMobi Meson Adapter]", "ad clicked")
            super.onAdClicked(p0, p1)
        }

        override fun onAdImpression(p0: InMobiInterstitial) {
            Log.i("[InMobi Meson Adapter]", "on impression")
            super.onAdImpression(p0)
        }

        override fun onAdFetchFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
            Log.e("[InMobi Meson Adapter]", "fetch fail")
            super.onAdFetchFailed(p0, p1)
        }

        override fun onAdDisplayed(p0: InMobiInterstitial, p1: AdMetaInfo) {
            Log.i("[InMobi Meson Adapter]", "ad displayed")
            super.onAdDisplayed(p0, p1)
        }

        override fun onAdDisplayFailed(p0: InMobiInterstitial) {
            Log.e("[InMobi Meson Adapter]", "ad display failed")
            super.onAdDisplayFailed(p0)
        }

        override fun onAdDismissed(p0: InMobiInterstitial) {
            Log.i("[InMobi Meson Adapter]", "ad dismissed")
            super.onAdDismissed(p0)
        }

        override fun onUserLeftApplication(p0: InMobiInterstitial) {
            Log.i("[InMobi Meson Adapter]", "user left")
            super.onUserLeftApplication(p0)
        }

        override fun onRewardsUnlocked(p0: InMobiInterstitial, p1: MutableMap<Any, Any>?) {
            Log.i("[InMobi Meson Adapter]", "rewards unlocked")
            super.onRewardsUnlocked(p0, p1)
        }
    })
}