package com.zjnu.dronefly

import android.app.Application
import android.content.Context
import com.secneo.sdk.Helper

class DroneApplication:Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Helper.install(this);
    }
}