// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// ignore_for_file: public_member_api_docs

import 'package:flutter/material.dart';
// #docregion DisplayMode
import 'package:zdc_maps_flutter_android/zdc_maps_flutter_android.dart';
import 'package:zdc_maps_flutter_platform_interface/zdc_maps_flutter_platform_interface.dart';

void main() {
  // Require Hybrid Composition mode on Android.
  final ZdcMapsFlutterPlatform mapsImplementation =
      ZdcMapsFlutterPlatform.instance;
  if (mapsImplementation is ZdcMapsFlutterAndroid) {
    mapsImplementation.useAndroidViewSurface = true;
  }
  // #enddocregion DisplayMode
  runApp(const MyApp());
  // #docregion DisplayMode
}
// #enddocregion DisplayMode

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('README snippet app'),
        ),
        body: const Text('See example in main.dart'),
      ),
    );
  }
}
