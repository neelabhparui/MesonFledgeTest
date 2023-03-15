package com.inmobi.meson.mesonsdk

import android.content.Context
import com.inmobi.meson.mesonsdk.listeners.MesonInitListener
import com.inmobi.meson.mesonsdk.mediation.MediationManager
import com.inmobi.meson.mesonsdk.mediation.NetworksManager
import com.inmobi.meson.mesonsdk.mock.isAccountIdIsValid
import java.lang.Error
import java.util.concurrent.atomic.AtomicBoolean

object MesonSdk {

    private var initialised = AtomicBoolean(false)

    fun init(context: Context, accId : String, listener: MesonInitListener) {
        //do init ops - example - validate with account id
        if (!isAccountIdIsValid(accId)) {
            listener.onComplete(
                Error("invalid account id")
            )
        }

        //get added networks
        NetworksManager.init(listener)

        //do other init stuff
        initialised.set(true)
    }

    fun isInitialised() = initialised.get()

    fun destroy() {
        NetworksManager.destroy()
        MediationManager.destroy()
        initialised.set(false)
    }
}