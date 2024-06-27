package com.example.device_special_info_plus

import android.content.Context
import android.provider.Settings

import com.scottyab.rootbeer.RootBeer


class JailBroken {
    fun isDevMode(context: Context): Boolean {
        return try {
            if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) == 16) {
                Settings.Secure.getInt(context.contentResolver,
                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
            } else if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 17) {
                Settings.Secure.getInt(context.contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
            } else
                false
        }catch (e:Exception){
            false
        }
    }

    fun isJailBroken(context: Context): Boolean{
        try {
            val rootBeer = RootBeer(context)
            return rootBeer.isRooted;
        }catch (e:Exception){
            return false
        }
    }
}