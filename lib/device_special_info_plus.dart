
import 'dart:io';

import 'package:device_special_info_plus/app_info.dart';
import 'package:device_special_info_plus/models/android_carrier_data.dart';

import 'device_special_info_plus_platform_interface.dart';

class DeviceSpecialInfoPlus {
  Future<String?> getPlatformVersion() {
    return DeviceSpecialInfoPlusPlatform.instance.getPlatformVersion();
  }

  Future<List<AppInfo>> getInstalledApps([
    bool excludeSystemApps = true,
    String packageNamePrefix = "",
  ]) {
    return DeviceSpecialInfoPlusPlatform.instance.getInstalledApps(excludeSystemApps,packageNamePrefix,);
  }

  Future<bool?> turnOnBluetooth() {
    return DeviceSpecialInfoPlusPlatform.instance.turnOnBluetooth();
  }

  Future<bool?> requestPermission() {
    return DeviceSpecialInfoPlusPlatform.instance.requestPermission();
  }

  Future<String?> uptime() {
    return DeviceSpecialInfoPlusPlatform.instance.uptime();
  }

  Future<String?> isRoamingEnabled() {
    return DeviceSpecialInfoPlusPlatform.instance.isRoamingEnabled();
  }

  Future<String?> getEnrollmentSpecificId({
    required String enterpriseID,
  }) {
    return DeviceSpecialInfoPlusPlatform.instance.getEnrollmentSpecificId(enterpriseID: enterpriseID);
  }
  Future<String?> getDeviceName() {
    return DeviceSpecialInfoPlusPlatform.instance.getDeviceName();
  }
  Future<String?> getBluetoothName() {
    return DeviceSpecialInfoPlusPlatform.instance.getBluetoothName();
  }

  Future<String?> getSerialNumber() {
    return DeviceSpecialInfoPlusPlatform.instance.getSerialNumber();
  }

  Future<String?> getImeiNumber() {
    return DeviceSpecialInfoPlusPlatform.instance.getImeiNumber();
  }

  Future<String?> getBluetoothMacAddress() {
    return DeviceSpecialInfoPlusPlatform.instance.getBluetoothMacAddress();
  }

  Future<Directory?> getDownloadsDirectory() {
    return DeviceSpecialInfoPlusPlatform.instance.getDownloadsDirectory();
  }

  Future<String> getIpAddress() {
    return DeviceSpecialInfoPlusPlatform.instance.getIpAddress();
  }

  Future<bool> isDevMode() {
    return DeviceSpecialInfoPlusPlatform.instance.isDevMode();
  }

  Future<bool> isJailBroken() {
    return DeviceSpecialInfoPlusPlatform.instance.isJailBroken();
  }

  Future<AndroidCarrierData?> getAndroidSimInfo() {
    return DeviceSpecialInfoPlusPlatform.instance.getAndroidSimInfo();
  }
}
