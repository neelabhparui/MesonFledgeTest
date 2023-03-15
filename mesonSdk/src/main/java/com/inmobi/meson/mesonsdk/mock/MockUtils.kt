package com.inmobi.meson.mesonsdk.mock

import android.util.Log
import com.inmobi.meson.mesonsdk.adapter.MesonAdapter
import com.inmobi.meson.mesonsdk.mediation.AdResponse
import com.inmobi.meson.mesonsdk.fledge.FledgeData
import com.inmobi.meson.mesonsdk.mediation.Network
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking


private const val INMOBI_NET_NAME = "InMobi"
private const val INMOBI_BUYER_HOST = ""
private const val THIRD_PARTY_NET_NAME = "ThirdParty"
private const val THIRD_PARTY_BUYER_HOST = ""
const val UNAVAILABLE_BID = -1

private const val INMOBI_ADAPTER_CLASSNAME = "com.inmobi.inmobiads.InMobiMesonAdapter"
private const val THIRD_PARTY_CLASSNAME = "com.example.thirdParty.ThirdPartyAdapter"
/**
 * This is a network call to meson server to get publisher settings including the networks they are integrated with.
 * Mocking it to a local call for now.
 */
suspend fun getNetworkListFromNetwork() = flow {
    val networks = HashMap<String, Network>()
    //simulating network delay
    runBlocking { delay(1000) }
    getNetwork(INMOBI_ADAPTER_CLASSNAME, INMOBI_NET_NAME)?.let {
        networks[INMOBI_NET_NAME] = it
    }
    getNetwork(THIRD_PARTY_CLASSNAME, THIRD_PARTY_NET_NAME)?.let {
        networks[THIRD_PARTY_NET_NAME] = it
    }
    emit(networks)
}

//suspend fun fetchFledgeData(placementId: String) = flow {
//    val data = ArrayList<FledgeData>()
//    //simulating network delay
//    runBlocking { delay(1000) }
//    emit(getFledgeData())
//}

fun isAccountIdIsValid(accId : String) : Boolean {
    if (accId == "") {
        return false
    }
    return true
}

private fun getFledgeData() = mapOf(
    INMOBI_NET_NAME to FledgeData("df19afdaf27f4fb4a2c2b85e2c10bc6a", listOf("")),
    THIRD_PARTY_NET_NAME to FledgeData("df19afdaf27f4fb4a2c2b85e2c10bc6a", listOf(""))
)

private fun getNetwork(className : String, name : String) : Network? {
    try {
        //getting adapter
        val obj = Class.forName(className).newInstance()
        if (obj is MesonAdapter) {

            //create network
            val network = object : Network {
                override fun getAdapter(): MesonAdapter {
                    return obj
                }

                override fun getName(): String {
                    return name
                }
            }
            return network
        } else {
            throw java.lang.IllegalArgumentException("classname passed is not child of MesonAdapter")
        }
    } catch (e : Exception) {
        Log.e("MOCK", "getNetwork Error", e)
    }
    return null
}

/**
 * This is a network call to meson server to get mediation chain/waterfall. Can contain both bidding and waterfall.
 * Mocking it to a local call for now.
 */
suspend fun getMediationChain(placementId : String) = flow {
    //simulating network delay
    runBlocking { delay(1000) }
    if (placementId == "") {
        Log.e("MOCK", "invalid placement id")
        return@flow
    }
    emit(chainMocks[1])
}

private fun getMockedInMobiJson() = ""

private val chainMocks = listOf(
            listOf(
                AdResponse(INMOBI_NET_NAME, getMockedInMobiJson(), 2, 10, "1623469928141", "df19afdaf27f4fb4a2c2b85e2c10bc6a"),
                AdResponse(THIRD_PARTY_NET_NAME, "", 4, UNAVAILABLE_BID, "1623469928141", "df19afdaf27f4fb4a2c2b85e2c10bc6a"),
                AdResponse(INMOBI_NET_NAME, "", 2, UNAVAILABLE_BID, "1623469928141", "df19afdaf27f4fb4a2c2b85e2c10bc6a")
            ),
            listOf(
                AdResponse(THIRD_PARTY_NET_NAME, "", 4, UNAVAILABLE_BID, "1621763331529", "df19afdaf27f4fb4a2c2b85e2c10bc6a"),
                AdResponse(INMOBI_NET_NAME, "", 2, UNAVAILABLE_BID, "1621763331529", "df19afdaf27f4fb4a2c2b85e2c10bc6a")
            )
        )