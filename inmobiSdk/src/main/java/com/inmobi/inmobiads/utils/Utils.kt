package com.inmobi.inmobiads.utils

import android.adservices.adselection.AdSelectionConfig
import android.adservices.adselection.AdSelectionManager
import android.adservices.adselection.AdSelectionOutcome
import android.adservices.common.AdSelectionSignals
import android.adservices.common.AdTechIdentifier
import android.content.Context
import android.net.Uri
import android.os.OutcomeReceiver
import android.util.Log
import com.inmobi.inmobiads.IM_ACTIVATION_TIME
import com.inmobi.inmobiads.IM_BUYER_BIDDING_URL
import com.inmobi.inmobiads.IM_CA_NAME
import com.inmobi.inmobiads.IM_DAILY_BIDDING_URL
import com.inmobi.inmobiads.IM_EXPIRY_TIME
import com.inmobi.inmobiads.IM_HOST
import com.inmobi.inmobiads.IM_KEY_LIST
import com.inmobi.inmobiads.IM_TRUSTED_BIDDING_URL
import com.inmobi.meson.mesonsdk.mediation.NetworksManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.stream.Collectors

private const val SCORING_URL = "https://0a95584a-b7a8-463b-92d0-5f558117a416.mock.pstmn.io/scoring"
private const val TRUSTED_SCORING_URL = "https://0a95584a-b7a8-463b-92d0-5f558117a416.mock.pstmn.io/scoring/trusted"
private val INMOBI_SSP_ID = Uri.parse(SCORING_URL).host!!.let {
    AdTechIdentifier.fromString(it)
}
private val EXECUTOR = Executors.newSingleThreadExecutor()

private var renderUrl : Uri? = null

private val DSPs = mutableListOf(
    AdTechIdentifier.fromString(Uri.parse(SCORING_URL).host!!)
)

fun getRenderUrl() = renderUrl

suspend fun fetchRemarketingData(dealId : String) = flow {
    //simulate network delay
    runBlocking {
        delay(1000)
    }
    if (dealId != "") {
        val jsonObject = JSONObject()
        jsonObject.put(IM_CA_NAME, "shoes")
        val jsonArray = JSONArray()
        jsonArray.put("key1")
        jsonArray.put("key2")
        jsonObject.put(IM_KEY_LIST, jsonArray)
        jsonObject.put(IM_DAILY_BIDDING_URL, "https://0a95584a-b7a8-463b-92d0-5f558117a416.mock.pstmn.io/bidding/daily")
        jsonObject.put(IM_EXPIRY_TIME, Instant.now().plus(Duration.ofDays(1)))
        jsonObject.put(IM_ACTIVATION_TIME, Instant.now())
        jsonObject.put(IM_TRUSTED_BIDDING_URL, "https://0a95584a-b7a8-463b-92d0-5f558117a416.mock.pstmn.io/bidding/trusted")
        jsonObject.put(IM_BUYER_BIDDING_URL, "https://0a95584a-b7a8-463b-92d0-5f558117a416.mock.pstmn.io/bidding")
        jsonObject.put(IM_HOST, "https://0a95584a-b7a8-463b-92d0-5f558117a416.mock.pstmn.io")
        emit(jsonObject)
    } else {
        emit(null)
    }

}

fun runFLEDGESelectAds(context : Context, callback : (error : Exception?) -> Unit) {
    try {
        val mAdSelectionManager = context.getSystemService(AdSelectionManager::class.java)
        Log.i("[INMOBI]", "DSPs - $DSPs")
        selectAds(mAdSelectionManager, callback)
    } catch (error : Exception) {
        Log.e("[INMOBI]", "fledge failed", error)
        callback(error)
    }
}

private fun selectAds(mAdSelectionManager : AdSelectionManager, callback : (error : Exception?) -> Unit) {
    mAdSelectionManager.selectAds(
        createConfig(),
        EXECUTOR,
        object : OutcomeReceiver<AdSelectionOutcome, Exception> {
            override fun onResult(result: AdSelectionOutcome) {
                Log.e("FLEDGE", "select ads - Success ${result.adSelectionId} ${result.renderUri}")
                callback(null)
                renderUrl = result.renderUri

            }

            override fun onError(error: Exception) {
                //  completer.setException(error)
                Log.e("FLEDGE", "select ads - Error selecting ads", error)
                callback(error)
                renderUrl = null
            }
        })
}

private fun createConfig() = AdSelectionConfig.Builder()
    .setSeller(INMOBI_SSP_ID)
    .setDecisionLogicUri(Uri.parse(SCORING_URL))
    .setCustomAudienceBuyers(DSPs)
    .setAdSelectionSignals(AdSelectionSignals.EMPTY)
    .setPerBuyerSignals(
        DSPs.stream()
        .collect(
            Collectors.toMap(
                { buyer: AdTechIdentifier -> buyer },
                { AdSelectionSignals.EMPTY })
        ))
    .setTrustedScoringSignalsUri(Uri.parse(TRUSTED_SCORING_URL))
    .build()