import 'dart:io';

import 'package:device_special_info_plus/app_info.dart';
import 'package:device_special_info_plus/models/android_carrier_data.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'device_special_info_plus_platform_interface.dart';

/// An implementation of [DeviceSpecialInfoPlusPlatform] that uses method channels.
class MethodChannelDeviceSpecialInfoPlus extends DeviceSpecialInfoPlusPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('device_special_info_plus');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<List<AppInfo>> getInstalledApps([
    bool excludeSystemApps = true,
    String packageNamePrefix = "",
  ]) async {
    List<dynamic> result = await methodChannel.invokeMethod(
      'getInstalledApps',
      {
        "exclude_system_apps": excludeSystemApps,
        "package_name_prefix": packageNamePrefix,
      },
    );
    List<AppInfo> appInfoList = result.map((app) => AppInfo.create(app)).toList();
    appInfoList.sort(
      (a, b) => (a.name ?? "").compareTo(b.name ?? ""),
    );
    return appInfoList;
  }

  @override
  Future<bool> turnOnBluetooth() async {
    final bool? result = await methodChannel.invokeMethod('turnOnBluetooth');
    return result??false;
  }

  @override
  Future<bool> requestPermission() async {
    final bool? result = await methodChannel.invokeMethod('requestPermission');
    return result??false;
  }

  @override
  Future<String> uptime() async {
    final int? result = await methodChannel.invokeMethod('getUptime');
    var uptimeDuration = Duration(milliseconds: result??0);
    return uptimeDuration.toString();
  }

  @override
  Future<String> isRoamingEnabled() async {
    bool? result = await methodChannel.invokeMethod('isDataRoamingEnabled');
    return (result ?? false) ? "Yes" : "No";
  }

  @override
  Future<String> getEnrollmentSpecificId({
    required String enterpriseID,
  }) async {
    final String? enrollmentSpecificId = await methodChannel.invokeMethod(
      'enrollmentSpecificId',
      {
        "enterpriseID": enterpriseID,
      },
    );
    return enrollmentSpecificId ?? "";
  }

  @override
  Future<String> getDeviceName() async {
    final String? deviceName = await methodChannel.invokeMethod('deviceName');
    return deviceName??"";
  }

  @override
  Future<String> getBluetoothName() async {
    final String? bluetoothName = await methodChannel.invokeMethod('bluetoothName');
    return bluetoothName??"";
  }

  @override
  Future<String> getBluetoothMacAddress() async {
    final result = await methodChannel.invokeMethod('getBluetoothMacAddress');
    return result ?? "";
  }

  @override
  Future<String> getImeiNumber() async {
    final result = await methodChannel.invokeMethod('getIMEI');
    return result ?? "";
  }

  @override
  Future<String> getSerialNumber() async {
    final result = await methodChannel.invokeMethod('getSerialNumber');
    return result ?? "";
  }

  @override
  Future<Directory?> getDownloadsDirectory() async {
    String? result = await methodChannel.invokeMethod('getDownloadsDirectory');
    Directory? directory;
    if((result??"").isNotEmpty){
      directory = Directory(result??"");
    }
    return directory;
  }

  @override
  Future<String> getIpAddress() async {
    final result = await methodChannel.invokeMethod('getIpAddress');
    return result ?? "";
  }

  @override
  Future<bool> isDevMode() async {
    final result = await methodChannel.invokeMethod('isDevMode');
    return result ?? false;
  }

  @override
  Future<bool> isJailBroken() async {
    final result = await methodChannel.invokeMethod('isJailBroken');
    return result ?? false;
  }

  @override
  Future<AndroidCarrierData?> getAndroidSimInfo() async {
    try{
      AndroidCarrierData? androidCarrierData;
      final result = await methodChannel.invokeMethod('getAndroidSimInfo');
      if(result!=null){

        androidCarrierData = AndroidCarrierData.fromMap(result??{},);
      }
      return androidCarrierData;
    }catch(e){
      return null;
    }
  }
}
