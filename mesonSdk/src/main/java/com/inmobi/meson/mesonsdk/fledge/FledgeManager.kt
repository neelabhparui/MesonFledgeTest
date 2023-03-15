package com.inmobi.meson.mesonsdk.fledge

import android.adservices.adselection.AdSelectionConfig
import android.adservices.adselection.AdSelectionManager
import android.adservices.adselection.AdSelectionOutcome
import android.adservices.common.AdSelectionSignals
import android.adservices.common.AdTechIdentifier
import android.app.Activity
import android.net.Uri
import android.os.OutcomeReceiver
import android.util.Log
import com.inmobi.meson.mesonsdk.adapter.MesonAdapter
import com.inmobi.meson.mesonsdk.mediation.MediationManager
import com.inmobi.meson.mesonsdk.mediation.NetworksManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Error
import java.util.concurrent.Executors
import java.util.stream.Collectors

private const val SCORING_URL = ""
private const val TRUSTED_SCORING_URL = ""
private const val DEFAULT_SELLER_ID = ""

object FledgeManager {

//    private val EXECUTOR = Executors.newSingleThreadExecutor()
//
//    private val scope = CoroutineScope(Dispatchers.IO)
//
//    private val MESON_SSP_ID = Uri.parse(SCORING_URL).host?.let {
//        AdTechIdentifier.fromString(it)
//    } ?: AdTechIdentifier.fromString(DEFAULT_SELLER_ID)
//
//    fun selectAdsFromFledge(activity : Activity, placementId : String, listener: SelectAdsListener) {
//        try {
//            val mAdSelectionManager = activity.getSystemService(AdSelectionManager::class.java)
//            setupFledge(placementId) {
//                selectAds(mAdSelectionManager, listener)
//            }
//
//        } catch (error : java.lang.Exception) {
//            Log.e("FLEDGE", "fledge failed", error)
//            listener.onComplete(null, error)
//        }
//    }
//
//    private fun setupFledge(placementId: String, postOp: () -> Unit) {
//        scope.launch {
//            NetworksManager.fetchRemarketingData(placementId).collect {
//                postOp()
//            }
//        }
//    }
//
//    private fun selectAds(mAdSelectionManager : AdSelectionManager, listener: SelectAdsListener) {
//        mAdSelectionManager.selectAds(
//            createConfig(),
//            EXECUTOR,
//            object : OutcomeReceiver<AdSelectionOutcome, java.lang.Exception> {
//                override fun onResult(result: AdSelectionOutcome) {
//                    Log.e("FLEDGE", "select ads - Success ${result.adSelectionId} ${result.renderUri}")
//                    listener.onComplete(result, null)
//                }
//
//                override fun onError(error: java.lang.Exception) {
//                    //  completer.setException(error)
//                    Log.e("FLEDGE", "select ads - Error selecting ads", error)
//                    listener.onComplete(null, error)
//                }
//            })
//    }
//
//    private fun createConfig() = AdSelectionConfig.Builder()
//                                .setSeller(MESON_SSP_ID)
//                                .setDecisionLogicUri(Uri.parse(SCORING_URL))
//                                .setCustomAudienceBuyers(NetworksManager.getBuyerList())
//                                .setAdSelectionSignals(AdSelectionSignals.EMPTY)
//                                .setPerBuyerSignals(NetworksManager.getBuyerList().stream()
//                                    .collect(
//                                        Collectors.toMap(
//                                            { buyer: AdTechIdentifier -> buyer },
//                                            { AdSelectionSignals.EMPTY })
//                                    ))
//                                .setTrustedScoringSignalsUri(Uri.parse(TRUSTED_SCORING_URL))
//                                .build()
//
//    interface SelectAdsListener {
//        fun onComplete(outcome : AdSelectionOutcome?, error: java.lang.Exception? = null)
//    }
}