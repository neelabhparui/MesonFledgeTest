package com.inmobi.meson.mesonsdk.mediation

import com.inmobi.meson.mesonsdk.adapter.MesonAdapter
import org.json.JSONObject

interface Network {
    fun getAdapter() : MesonAdapter
    fun getName() : String
}