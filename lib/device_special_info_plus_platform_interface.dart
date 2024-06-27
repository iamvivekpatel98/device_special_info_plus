import 'dart:io';

import 'package:device_special_info_plus/app_info.dart';
import 'package:device_special_info_plus/models/android_carrier_data.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'device_special_info_plus_method_channel.dart';

abstract class DeviceSpecialInfoPlusPlatform extends PlatformInterface {
  /// Constructs a DeviceSpecialInfoPlusPlatform.
  DeviceSpecialInfoPlusPlatform() : super(token: _token);

  static final Object _token = Object();

  static DeviceSpecialInfoPlusPlatform _instance =
      MethodChannelDeviceSpecialInfoPlus();

  /// The default instance of [DeviceSpecialInfoPlusPlatform] to use.
  ///
  /// Defaults to [MethodChannelDeviceSpecialInfoPlus].
  static DeviceSpecialInfoPlusPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DeviceSpecialInfoPlusPlatform] when
  /// they register themselves.
  static set instance(DeviceSpecialInfoPlusPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> getSerialNumber() {
    throw UnimplementedError('getSerialNumber() has not been implemented.');
  }

  Future<String?> getImeiNumber() {
    throw UnimplementedError('getImeiNumber() has not been implemented.');
  }

  Future<String?> getBluetoothMacAddress() {
    throw UnimplementedError('getBluetoothMacAddress() has not been implemented.');
  }

  Future<String?> getBluetoothName() {
    throw UnimplementedError('getBluetoothName() has not been implemented.');
  }

  Future<String?> getDeviceName() {
    throw UnimplementedError('getDeviceName() has not been implemented.');
  }

  Future<String?> getEnrollmentSpecificId({
    required String enterpriseID,
  }) {
    throw UnimplementedError('getEnrollmentSpecificId() has not been implemented.');
  }

  Future<String?> isRoamingEnabled() {
    throw UnimplementedError('isRoamingEnabled() has not been implemented.');
  }

  Future<String?> uptime() {
    throw UnimplementedError('uptime() has not been implemented.');
  }

  Future<bool?> turnOnBluetooth() {
    throw UnimplementedError('turnOnBluetooth() has not been implemented.');
  }

  Future<bool?> requestPermission() {
    throw UnimplementedError('requestPermission() has not been implemented.');
  }

  Future<List<AppInfo>> getInstalledApps([
    bool excludeSystemApps = true,
    String packageNamePrefix = "",
  ]) {
    throw UnimplementedError('getInstalledApps() has not been implemented.');
  }

  Future<Directory?> getDownloadsDirectory() {
    throw UnimplementedError('getDownloadsDirectory() has not been implemented.');
  }

  Future<String> getIpAddress() {
    throw UnimplementedError('getIpAddress() has not been implemented.');
  }

  Future<bool> isDevMode() {
    throw UnimplementedError('isDevMode() has not been implemented.');
  }

  Future<bool> isJailBroken() {
    throw UnimplementedError('isJailBroken() has not been implemented.');
  }

  Future<AndroidCarrierData?> getAndroidSimInfo() {
    throw UnimplementedError('getAndroidSimInfo() has not been implemented.');
  }
}
