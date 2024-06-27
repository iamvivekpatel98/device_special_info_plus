import 'dart:io';

import 'package:device_special_info_plus/app_info.dart';
import 'package:device_special_info_plus/models/android_carrier_data.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:device_special_info_plus/device_special_info_plus.dart';
import 'package:device_special_info_plus/device_special_info_plus_platform_interface.dart';
import 'package:device_special_info_plus/device_special_info_plus_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDeviceSpecialInfoPlusPlatform
    with MockPlatformInterfaceMixin
    implements DeviceSpecialInfoPlusPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String?> getBluetoothMacAddress() {
    // TODO: implement getBluetoothMacAddress
    throw UnimplementedError();
  }

  @override
  Future<String?> getBluetoothName() {
    // TODO: implement getBluetoothName
    throw UnimplementedError();
  }

  @override
  Future<String?> getDeviceName() {
    // TODO: implement getDeviceName
    throw UnimplementedError();
  }

  @override
  Future<String?> getEnrollmentSpecificId({required String enterpriseID}) {
    // TODO: implement getEnrollmentSpecificId
    throw UnimplementedError();
  }

  @override
  Future<String?> getImeiNumber() {
    // TODO: implement getImeiNumber
    throw UnimplementedError();
  }

  @override
  Future<List<AppInfo>> getInstalledApps([bool excludeSystemApps = true, String packageNamePrefix = ""]) {
    // TODO: implement getInstalledApps
    throw UnimplementedError();
  }

  @override
  Future<String?> getSerialNumber() {
    // TODO: implement getSerialNumber
    throw UnimplementedError();
  }

  @override
  Future<String?> isRoamingEnabled() {
    // TODO: implement isRoamingEnabled
    throw UnimplementedError();
  }

  @override
  Future<bool?> turnOnBluetooth() {
    // TODO: implement turnOnBluetooth
    throw UnimplementedError();
  }

  @override
  Future<String?> uptime() {
    // TODO: implement uptime
    throw UnimplementedError();
  }

  @override
  Future<bool?> requestPermission() {
    // TODO: implement requestPermission
    throw UnimplementedError();
  }

  @override
  Future<Directory?> getDownloadsDirectory() {
    // TODO: implement getDownloadsDirectory
    throw UnimplementedError();
  }

  @override
  Future<String> getIpAddress() {
    // TODO: implement getIpAddress
    throw UnimplementedError();
  }

  @override
  Future<bool> isDevMode() {
    // TODO: implement isDevMode
    throw UnimplementedError();
  }

  @override
  Future<bool> isJailBroken() {
    // TODO: implement isJailBroken
    throw UnimplementedError();
  }

  @override
  Future<AndroidCarrierData?> getAndroidSimInfo() {
    // TODO: implement getAndroidSimInfo
    throw UnimplementedError();
  }
}

void main() {
  final DeviceSpecialInfoPlusPlatform initialPlatform = DeviceSpecialInfoPlusPlatform.instance;

  test('$MethodChannelDeviceSpecialInfoPlus is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDeviceSpecialInfoPlus>());
  });

  test('getPlatformVersion', () async {
    DeviceSpecialInfoPlus deviceSpecialInfoPlusPlugin = DeviceSpecialInfoPlus();
    MockDeviceSpecialInfoPlusPlatform fakePlatform = MockDeviceSpecialInfoPlusPlatform();
    DeviceSpecialInfoPlusPlatform.instance = fakePlatform;

    expect(await deviceSpecialInfoPlusPlugin.getPlatformVersion(), '42');
  });
}
