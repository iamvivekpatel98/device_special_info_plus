package com.example.device_special_info_plus

import android.content.Context
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import io.flutter.plugin.common.MethodChannel

class AndroidSimInfo {

     fun getInfo(
        result: MethodChannel.Result,
        applicationContext: Context,
        mDefaultTelephonyManager: TelephonyManager,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val telephonyList: ArrayList<HashMap<String, Any?>> = ArrayList()
            val subscriptionsList: ArrayList<HashMap<String, Any?>> = ArrayList()
            val subsManager =
                applicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            if (subsManager.activeSubscriptionInfoList != null) {

                for (subsInfo in subsManager.activeSubscriptionInfoList) {
                    if (subsInfo != null) {
                        try {
                            val data = hashMapOf(
                                "simSerialNo" to subsInfo.iccId,
                                "mobileCountryCode" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.mccString else subsInfo.mcc.toString(),
                                "countryIso" to subsInfo.countryIso,
                                "mobileNetworkCode" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.mncString else subsInfo.mnc.toString(),
                                "cardId" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.cardId else null,
                                "carrierId" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.carrierId else null,
                                "dataRoaming" to subsInfo.dataRoaming,
                                "isEmbedded" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.isEmbedded else null,
                                "simSlotIndex" to subsInfo.simSlotIndex,
                                "subscriptionType" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.subscriptionType else null,
                                "isOpportunistic" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) subsInfo.isOpportunistic else null,
                                "displayName" to subsInfo.displayName,
                                "subscriptionId" to subsInfo.subscriptionId,
                                "phoneNumber" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    subsManager.getPhoneNumber(subsInfo.subscriptionId) else subsInfo.number,
                                "isNetworkRoaming" to subsManager.isNetworkRoaming(subsInfo.subscriptionId),
                            )

                            subscriptionsList.add(data)
                        } catch (e: Exception) {
                            subscriptionsList.add(HashMap())
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val telephonyManager =
                                mDefaultTelephonyManager.createForSubscriptionId(subsInfo.subscriptionId);

                            try {
                                val data = hashMapOf<String, Any?>(
                                    "carrierName" to telephonyManager.simOperatorName,
                                    "dataActivity" to telephonyManager.dataActivity,
                                    "phoneNumber" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        subsManager.getPhoneNumber(subsInfo.subscriptionId) else subsInfo.number,
                                    "networkOperatorName" to telephonyManager.networkOperatorName,
                                    "subscriptionId" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) telephonyManager.subscriptionId else null,
                                    "isoCountryCode" to telephonyManager.simCountryIso,
                                    "networkCountryIso" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) telephonyManager.networkCountryIso else null,
                                    "mobileNetworkCode" to if (telephonyManager.simOperator?.isEmpty() == false)
                                        telephonyManager.simOperator?.substring(3) else null,
                                    "displayName" to telephonyManager.simOperatorName,
                                    "mobileCountryCode" to if (telephonyManager.simOperator?.isEmpty() == false)
                                        telephonyManager.simOperator?.substring(0, 3) else null,
                                )

                                telephonyList.add(data)
                            } catch (e: Exception) {
                                telephonyList.add(HashMap())
                            }
                        }
                    }
                }
            }

            val data = hashMapOf<String, Any?>(
                "isDataEnabled" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) mDefaultTelephonyManager.isDataEnabled else null,
                "isDataCapable" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) mDefaultTelephonyManager.isDataCapable else null,
                "isSmsCapable" to mDefaultTelephonyManager.isSmsCapable,
                "isVoiceCapable" to mDefaultTelephonyManager.isVoiceCapable,
                "telephonyInfo" to telephonyList,
                "subscriptionsInfo" to subscriptionsList,
            )

            result.success(data)
        } else {
            result.success(null)
        }


    }

}