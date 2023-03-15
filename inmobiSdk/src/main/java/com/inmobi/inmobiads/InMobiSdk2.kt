package com.inmobi.inmobiads

import android.adservices.common.AdTechIdentifier
import android.adservices.customaudience.CustomAudience
import android.adservices.customaudience.CustomAudienceManager
import android.adservices.customaudience.JoinCustomAudienceRequest
import android.adservices.customaudience.TrustedBiddingData
import android.content.Context
import android.net.Uri
import android.os.OutcomeReceiver
import android.util.Log
import com.inmobi.inmobiads.utils.fetchRemarketingData
import com.inmobi.sdk.InMobiSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

const val IM_CA_NAME = "im-ca-name"
const val IM_BUYER_BIDDING_URL = "im-buyer-bidding-url"
const val IM_TRUSTED_BIDDING_URL = "im-trusted-bidding-url"
const val IM_DAILY_BIDDING_URL = "im-daily-bidding-url"
const val IM_KEY_LIST = "im-key-list"
const val IM_ACTIVATION_TIME = "im-activation-time"
const val IM_EXPIRY_TIME = "im-expiry-time"
const val IM_HOST = "im-host"
object InMobiSdk2 {

    private val EXECUTOR = Executors.newSingleThreadExecutor()

    fun targetForRemarketing(context: Context, dealId : String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("INMOBI2", "getting remarketing data")
            if (InMobiSdk.isSDKInitialized()) {
                fetchRemarketingData(dealId).collect{obj ->
                    obj?.let {
                        handleRemarketingSetup(context, it)
                    }
                }
            } else {
                Log.i("INMOBI2", "MESON SDK not init")
            }
        }
    }

    private fun handleRemarketingSetup(context : Context, jsonObject: JSONObject) {
        val customAudienceManager = context.getSystemService(
            CustomAudienceManager::class.java
        )
        val activation = Instant.parse(jsonObject.getString(IM_ACTIVATION_TIME))
        val expiry = Instant.parse(jsonObject.getString(IM_EXPIRY_TIME))
        Log.i("INMOBI2", "activation time - $activation , expiry - $expiry")
        val buyer = AdTechIdentifier.fromString(
            Uri.parse(jsonObject.getString(IM_HOST)).host!!)
        Log.i("INMOBI2", "buyer - $buyer")
        val biddingUri = Uri.parse(jsonObject.getString(IM_BUYER_BIDDING_URL).trim())
        val dailyUri = Uri.parse(jsonObject.getString(IM_DAILY_BIDDING_URL).trim())
        val trustedUri = Uri.parse(jsonObject.getString(IM_TRUSTED_BIDDING_URL).trim())
        val keys = getKeys(jsonObject.getJSONArray(IM_KEY_LIST))
        val name = jsonObject.getString(IM_CA_NAME)
        Log.i("INMOBI2", "Uris - bidding - $biddingUri \n daily - $dailyUri \n trusted - $trustedUri")
        Log.i("INMOBI2", "keys - $keys")
        Log.i("INMOBI2", "name - $name")
        val ca = CustomAudience.Builder()
            .setName(name)
            .setBuyer(buyer)
            .setBiddingLogicUri(biddingUri)
            .setDailyUpdateUri(dailyUri)
            .setTrustedBiddingData(
                TrustedBiddingData.Builder()
                    .setTrustedBiddingKeys(keys)
                    .setTrustedBiddingUri(trustedUri)
                    .build())
            .setActivationTime(activation)
            .setExpirationTime(expiry)
            .build()
        val request = JoinCustomAudienceRequest.Builder()
            .setCustomAudience(ca)
            .build()
        customAudienceManager.joinCustomAudience(request, EXECUTOR, object :
            OutcomeReceiver<Any?, Exception?> {
            override fun onError(error: Exception) {
                Log.e("INMOBI2", "join CA - Fail -${error.printStackTrace()}", error)
            }

            override fun onResult(p0: Any) {
                Log.i("INMOBI2", "join CA - Success")
                // updateBuilder.append("${p0}\n")
            }
        })
    }

    private fun getKeys(jsonArray: JSONArray) : List<String> {
        val keys = ArrayList<String>()
        for (index in 0 until jsonArray.length()) {
            keys.add(jsonArray.getString(index))
        }
        return keys
    }
}