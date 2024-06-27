import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:device_special_info_plus/device_special_info_plus.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _deviceSpecialInfoPlusPlugin = DeviceSpecialInfoPlus();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _deviceSpecialInfoPlusPlugin.getPlatformVersion() ??
              'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('Running on: $_platformVersion\n'),
              TextButton(
                onPressed: () async {

                  // dynamic getInstalledApps = await _deviceSpecialInfoPlusPlugin.getInstalledApps();
                  // dynamic getSerialNumber = await _deviceSpecialInfoPlusPlugin.getSerialNumber();
                  // dynamic getImeiNumber = await _deviceSpecialInfoPlusPlugin.getImeiNumber();
                  // dynamic getDeviceName = await _deviceSpecialInfoPlusPlugin.getDeviceName();
                  // dynamic getBluetoothName = await _deviceSpecialInfoPlusPlugin.getBluetoothName();
                  // dynamic getBluetoothMacAddress = await _deviceSpecialInfoPlusPlugin.getBluetoothMacAddress();
                  // dynamic getPlatformVersion = await _deviceSpecialInfoPlusPlugin.getPlatformVersion();
                  // dynamic isRoamingEnabled = await _deviceSpecialInfoPlusPlugin.isRoamingEnabled();
                  // dynamic uptime = await _deviceSpecialInfoPlusPlugin.uptime();
                  // dynamic turnOnBluetooth = await _deviceSpecialInfoPlusPlugin.turnOnBluetooth();
                  // dynamic requestPermission = await _deviceSpecialInfoPlusPlugin.requestPermission();
                  // Directory? getDownloadsDirectory = await _deviceSpecialInfoPlusPlugin.getDownloadsDirectory();
                  // dynamic getIpAddress = await _deviceSpecialInfoPlusPlugin.getIpAddress();
                  // dynamic isDevMode = await _deviceSpecialInfoPlusPlugin.isDevMode();
                  // dynamic isJailBroken = await _deviceSpecialInfoPlusPlugin.isJailBroken();
                  // dynamic getAndroidSimInfo = await _deviceSpecialInfoPlusPlugin.getAndroidSimInfo();
                  // debugPrint("result:: ${getAndroidSimInfo.toString()}");
                },
                child: const Text(
                  "Click Here!",
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
