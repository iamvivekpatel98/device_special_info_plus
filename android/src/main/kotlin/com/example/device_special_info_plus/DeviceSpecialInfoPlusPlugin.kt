package com.example.device_special_info_plus

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import android.os.Environment
import android.os.SystemClock
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.lang.reflect.Field
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale


/** DeviceSpecialInfoPlusPlugin */
class DeviceSpecialInfoPlusPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var applicationContext: Context? = null
    private var activity: Activity? = null
    private var mDefaultTelephonyManager: TelephonyManager? = null
    private lateinit var myDevice: BluetoothAdapter

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.applicationContext = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "device_special_info_plus")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "getSerialNumber") {
            val serial = getSerialNumber();
            if (serial != "") {
                if (result != null) {
                    result.success(serial)
                }
            } else {
                if (result != null) {
                    result.error("UNAVAILABLE", "Serial Number not available.", null)
                }
            }
        } else if (call.method == "isDataRoamingEnabled") {
            val isRoaming = applicationContext?.let { isDataRoamingEnabled(it) }
            if (result != null) {
                result.success(isRoaming)
            }
        } else if (call.method == "getIMEI") {
            val imeiNumber = applicationContext?.let { getIMEI(it) }
            if (result != null) {
                result.success(imeiNumber)
            }
        } else if (call.method == "getInstalledApps") {
            val includeSystemApps = call.argument("exclude_system_apps") ?: true
            val packageNamePrefix: String = call.argument("package_name_prefix") ?: ""
            Thread {
                val apps: List<Map<String, Any?>> = getInstalledApps(includeSystemApps, packageNamePrefix)
                result.success(apps)
            }.start()
        } else if (call.method == "getBluetoothMacAddress") {
            val bluetoothMac = getBluetoothMacAddress()
            if (result != null) {
                result.success(bluetoothMac)
            }
        } else if (call.method == "getUptime") {
            result?.let { getUptime(result) }
        } else if (call.method == "turnOnBluetooth") {
            try {
                myDevice = BluetoothAdapter.getDefaultAdapter()
                if (!myDevice.isEnabled) {
                    var status = myDevice.enable();
                    result?.let { result.success(status) }
                } else {
                    result?.let { result.success(false) }
                }
            }catch (e:Exception){
                result?.let { result.success(false) }
            }
        } else if (call.method == "bluetoothName") {
            try {
                myDevice = BluetoothAdapter.getDefaultAdapter()
                var deviceName: String = ""
                if (myDevice.isEnabled) {
                    deviceName = myDevice.name
                    Log.v("ON Bluetooth Name", "system device_name: $deviceName");
                    result?.let { result.success(deviceName) }
                } else {
                    myDevice.enable()
                    deviceName = myDevice.name
                    Log.v("OFF Bluetooth Name", "system device_name: $deviceName");
                    result?.let { result.success(deviceName) }
                }
            }catch (e:Exception){
                result?.let { result.success("") }
            }
        } else if (call.method == "deviceName") {
            try {
                if (Build.VERSION.SDK_INT >= 31) {
                    Log.i(
                        "ITSM",
                        "DEVICE NAME : ${
                            Settings.Global.getString(
                                applicationContext?.contentResolver,
                                Settings.Global.DEVICE_NAME
                            )
                        }"
                    )
                    val name = Settings.Global.getString(
                        applicationContext?.contentResolver,
                        Settings.Global.DEVICE_NAME
                    )
                    result?.let { result.success(name) }
                } else {
                    result?.let { result.success("") }
                }
            }catch (e:Exception){
                result?.let { result.success("") }
            }
        } else if (call.method == "enrollmentSpecificId") {
            try{
                val eId = call.argument("enterpriseID") ?: ""
                if (Build.VERSION.SDK_INT >= 31) {
                    val manager =
                        applicationContext?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    if (manager.isProfileOwnerApp(applicationContext?.packageName)) {
                        val enterpriseID = eId
                        manager.setOrganizationId(enterpriseID)
                        if (manager.enrollmentSpecificId.isNotEmpty()) {
                            result?.success(manager.enrollmentSpecificId)
                        } else result?.success("")
                    } else result?.success("")
                } else result?.success("")
            }catch (e:Exception){
                result?.success("")
            }
        } else if (call.method.equals("requestPermission")) {
            if (isAPI23Up()) {
                activity?.let { requestPermission(it) }
                result?.success(true);
            } else {
                val map = HashMap<String, Boolean>()
                map["status"] = true;
                map["neverAskAgain"] = false;

                result?.success(true);
            }
            return;

        } else if (call.method.equals("getDownloadsDirectory")) {
            result?.success(getDownloadsDirectory());
        }else if (call.method.equals("getIpAddress")) {
            result?.success(IPAddress().getIPAddress(true));
        }else if (call.method.equals("isDevMode")) {
            result?.success(applicationContext?.let { JailBroken().isDevMode(it) });
        }else if (call.method.equals("isJailBroken")) {
            result?.success(applicationContext?.let { JailBroken().isJailBroken(it) });
        }else if (call.method.equals("getAndroidSimInfo")) {
            mDefaultTelephonyManager = applicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if(mDefaultTelephonyManager!=null){
                applicationContext?.let {
                    AndroidSimInfo().getInfo(result, it, mDefaultTelephonyManager!!);
                }
            }
        } else if (call.method.equals("getNetworkConnectionType")) {
            result?.success(applicationContext?.let { getNetworkConnectionType(it) });
        } else if (call.method.equals("getMacAddress")) {
            result?.success(getMacAddress());
        } else {
            result.notImplemented()
        }
    }


    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }
    override fun onReattachedToActivityForConfigChanges(binding:ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onDetachedFromActivity() {}

    companion object {
        fun convertAppToMap(
            packageManager: PackageManager,
            app: ApplicationInfo,
        ): HashMap<String, Any?> {
            val map = HashMap<String, Any?>()
            map["name"] = packageManager.getApplicationLabel(app)
            map["package_name"] = app.packageName
            /*map["icon"] =
                    if (withIcon) drawableToByteArray(app.loadIcon(packageManager)) else ByteArray(0)*/
            val packageInfo = packageManager.getPackageInfo(app.packageName, 0)
            map["version_name"] = packageInfo.versionName
            map["version_code"] = getVersionCode(packageInfo)
            return map
        }

        @Suppress("DEPRECATION")
        private fun getVersionCode(packageInfo: PackageInfo): Long {
            return if (SDK_INT < P) packageInfo.versionCode.toLong()
            else packageInfo.longVersionCode
        }
    }

    @SuppressLint("AnnotateVersionCheck")
    private fun isAPI23Up(): Boolean {
        return SDK_INT >= Build.VERSION_CODES.M
    }

    @SuppressLint("LongLogTag")
    private fun requestPermission(thisActivity: Activity) {
        ActivityCompat.requestPermissions(
            thisActivity, arrayOf<String>(Manifest.permission.READ_PHONE_STATE),
            0
        )
    }


    private fun getUptime(result: MethodChannel.Result) {
        result.success(SystemClock.elapsedRealtime())
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getSerialNumber(): String {
        try{
            val serialNumber: String = if (Build.VERSION.SDK_INT >= 26) {
                android.os.Build.getSerial();
            } else /*if(Build.VERSION.SDK_INT <= 25)*/ {
                android.os.Build.SERIAL;
            }
            return serialNumber
        }catch (e:Exception){
            return ""
        }
    }

    @SuppressLint("HardwareIds", "LongLogTag")
    private fun getIMEI(c: Context): String {
        try{
            val telephonyManager: TelephonyManager =
                c.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val deviceId: String = if (telephonyManager.deviceId == null) {
                "returned null"
            } else {
                telephonyManager.deviceId
            }
            return deviceId
        }catch (e:Exception){
            return ""
        }
    }

    private fun getInstalledApps(
        excludeSystemApps: Boolean,
        packageNamePrefix: String
    ): List<Map<String, Any?>> {
        try {
            val packageManager = applicationContext!!.packageManager
            var installedApps = packageManager.getInstalledApplications(0)
            if (excludeSystemApps)
                installedApps =
                    installedApps.filter { app -> !isSystemApp(packageManager, app.packageName) }
            if (packageNamePrefix.isNotEmpty())
                installedApps = installedApps.filter { app ->
                    app.packageName.startsWith(
                        packageNamePrefix.lowercase(Locale.ENGLISH)
                    )
                }
            return installedApps.map { app -> convertAppToMap(packageManager, app) }
        }catch (e:Exception){
            return emptyList()
        }
    }

    private fun isSystemApp(packageManager: PackageManager, packageName: String): Boolean {
        return packageManager.getLaunchIntentForPackage(packageName) == null
    }


    private fun isDataRoamingEnabled(applicationContext: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT < 17) {
                Settings.System.getInt(
                    applicationContext.contentResolver,
                    Settings.Secure.DATA_ROAMING,
                    0
                ) === 1
            } else Settings.Global.getInt(
                applicationContext.contentResolver,
                Settings.Global.DATA_ROAMING,
                0
            ) === 1
        } catch (exception: Exception) {
            false
        }
    }

    @SuppressLint("PrivateApi")
    private fun getBluetoothMacAddress(): String {
        var bluetoothMacAddress: String
        try {
            /*var bluetoothManager:BluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothMacAddress=bluetoothManager.adapter.address*/

            /*bluetoothMacAddress = Settings.Secure.getString(context.contentResolver, "bluetooth_address")*/

            val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val mServiceField: Field = bluetoothAdapter.javaClass.getDeclaredField("mService")
            mServiceField.isAccessible = true
            val btManagerService: Any? = mServiceField.get(bluetoothAdapter)
            bluetoothMacAddress = if (btManagerService != null) {
                btManagerService.javaClass.getMethod("getAddress")
                    .invoke(btManagerService) as String
            } else {
                ""
            }
        } catch (exception: Exception) {
            bluetoothMacAddress = ""
        }
        return bluetoothMacAddress
    }

    private fun getDownloadsDirectory(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
    }

    private fun getMacAddress(): String {
        try {
            val all: List<NetworkInterface> = Collections.list(
                NetworkInterface.getNetworkInterfaces()
            )
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                val macBytes = nif.hardwareAddress ?: return ""

                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(
                        String.format("%1$2s", Integer.toHexString(b.toInt() and 0xFF))
                            .replace(' ', '0') +
                                ":"
                    )
                }

                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: java.lang.Exception) {
            return ""
        }
        return ""
    }

    private fun getNetworkConnectionType(applicationContext: Context): String {
        var result = "unknown"

        val cm = applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                val capabilities = cm.getNetworkCapabilities(
                    cm.activeNetwork
                )
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = "Wi-Fi connection"
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    ) {
                        result = "Mobile data"
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    ) {
                        result = "VPN"
                    }
                }
            } else {
                result = "unknown"
            }
        } else {
            if (cm != null) {
                val activeNetwork = cm.activeNetworkInfo
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                        result = "Wi-Fi connection"
                    } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE
                    ) {
                        result = "Mobile data"
                    } else if (activeNetwork.type == ConnectivityManager.TYPE_VPN) {
                        result = "VPN"
                    }
                } else {
                    result = "unknown"
                }
            }
        }

        return result
    }
}