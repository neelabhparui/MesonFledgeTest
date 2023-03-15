package com.inmobi.meson.mesonsdk.mediation

import android.adservices.common.AdTechIdentifier
import android.util.Log
import com.inmobi.meson.mesonsdk.fledge.FledgeData
import com.inmobi.meson.mesonsdk.listeners.MesonInitListener
import com.inmobi.meson.mesonsdk.mock.getNetworkListFromNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

object NetworksManager {
    private val networks = HashMap<String, Network>()
    private val networkFledgeData = HashMap<String, FledgeData>()
    val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var buyers : ArrayList<AdTechIdentifier>

    fun init(listener: MesonInitListener) {
        //fetch list of networks from server
        scope.launch {
            getNetworkListFromNetwork().collect{networks ->
                Log.i("MESON", "get networks - ${networks.size}")
                NetworksManager.networks.putAll(networks)
                listener.onComplete(null)
            }
        }
    }

    fun destroy() {
        scope.cancel()
        networks.clear()
        networkFledgeData.clear()
    }

    fun getBuyerList() : List<AdTechIdentifier> {
        if (this::buyers.isInitialized) {
            return buyers
        }
        buyers = ArrayList()
        networkFledgeData.forEach {
            buyers.addAll(getBuyerIdList(it.value))
        }
        return buyers
    }

    private fun getBuyerIdList(fledgeData: FledgeData) : List<AdTechIdentifier> {
        val buyers = ArrayList<AdTechIdentifier>()
        fledgeData.buyerList.forEach {
            buyers.add(AdTechIdentifier.fromString(it))
        }
        return buyers
    }

//    fun getNetworkFromUri(uri: Uri) : Network? {
//        networkFledgeData.forEach {
//            if (it.value.buyerList.contains(uri.host)) {
//                return networks[it.key]
//            }
//        }
//        return null
//    }
//
//    fun fetchRemarketingData(placementId : String) = flow{
//        scope.launch {
//            fetchFledgeData(placementId).collect {
//                networkFledgeData.clear()
//                networkFledgeData.putAll(it)
//                emit(it)
//            }
//        }
//    }


    fun getNetwork(name : String) = networks[name]
}