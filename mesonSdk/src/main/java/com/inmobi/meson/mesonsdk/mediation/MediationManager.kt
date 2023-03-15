package com.inmobi.meson.mesonsdk.mediation

import android.adservices.adselection.AdSelectionOutcome
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.inmobi.meson.mesonsdk.adapter.MesonAdapter
import com.inmobi.meson.mesonsdk.mock.UNAVAILABLE_BID
import com.inmobi.meson.mesonsdk.mock.getMediationChain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Error

object MediationManager {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var tryNext = true

    private val adLoadListener = object : MesonAdapter.AdLoadListener {
        override fun loaded() {
            //fire ad loaded events
            tryNext = false
        }

        override fun shown() {
            //fire ad shown events
        }

        override fun showFail() {
            //fire ad show failed events
        }

        override fun loadFail() {
            //fire ad load failed events
            tryNext = true
        }

        override fun dismissed() {
            //fire ad dismissed events
        }

        override fun impressed() {
            //fire ad impression events
        }

    }

    fun  loadBanner(activity: Activity, parent : ViewGroup, placementId : String) {
        //do initial checks
        Log.i("MediationManager", "getting mediation chain for plc - $placementId")
        //fetch chain from server\
        scope.launch {
            getMediationChain(placementId).collect {responses ->
                Log.i("MediationManager", "got chain - ${responses.size}")
                tryNext = true
                var index = 0
                while (tryNext && index < responses.size) {
                    val response = responses[index]
                    Log.i("MediationManager", "getting network data for - ${response.networkName}")
                    val network = NetworksManager.getNetwork(response.networkName)
                    network?.let {
                        network.getAdapter().init(activity, response.networkAccId, JSONObject(), object : MesonAdapter.OnCompletionListener{
                            override fun onComplete(error: Error?) {
                                error?.let {
                                    Log.e("[${network.getName()}]", "network init failed", it)
                                } ?: kotlin.run {
                                    Log.i("[${network.getName()}]", "network init success")
                                    if (isBidding(response)) { //check if bidding or waterfall.
                                        parent.addView(it.getAdapter().loadBanner(
                                            activity,
                                            response.networkPlcId,
                                            response.content.toByteArray(),
                                            adLoadListener
                                        ))
                                    } else {
                                        parent.addView(it.getAdapter().loadBanner(
                                            activity,
                                            response.networkPlcId,
                                            adLoadListener
                                        ))
                                    }
                                }
                            }

                        })
                    } ?: Log.e("MEDIATE", "cannot load ${response.networkName}")
                    index ++
                }
            }
        }
    }

//    fun loadFromFledgeOutcome(context: Context, parent: ViewGroup, outcome : AdSelectionOutcome) {
//        val network = NetworksManager.getNetworkFromUri(outcome.renderUri)
//        parent.addView(network?.getAdapter()?.loadBanner(context, outcome.renderUri, adLoadListener))
//    }
//
//    fun loadFromFledgeOutcome(context: Context, outcome : AdSelectionOutcome) {
//        val network = NetworksManager.getNetworkFromUri(outcome.renderUri)
//        network?.getAdapter()?.loadInterstitial(context, outcome.renderUri, adLoadListener)
//    }

    fun destroy() {
        scope.cancel()
    }

    private fun isBidding(response : AdResponse) = response.realTimeBid != UNAVAILABLE_BID //dummy. actually might be more complex
}