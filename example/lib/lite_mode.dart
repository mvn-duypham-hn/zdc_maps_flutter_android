// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// ignore_for_file: public_member_api_docs

import 'package:flutter/material.dart';
import 'package:zdc_maps_flutter_platform_interface/zdc_maps_flutter_platform_interface.dart';

import 'example_zdc_map.dart';
import 'page.dart';

const CameraPosition _kInitialPosition =
    CameraPosition(target: LatLng(-33.852, 151.211), zoom: 11.0);

class LiteModePage extends ZdcMapExampleAppPage {
  const LiteModePage({Key? key})
      : super(const Icon(Icons.map), 'Lite mode', key: key);

  @override
  Widget build(BuildContext context) {
    return const _LiteModeBody();
  }
}

class _LiteModeBody extends StatelessWidget {
  const _LiteModeBody();

  @override
  Widget build(BuildContext context) {
    return const Card(
      child: Padding(
        padding: EdgeInsets.symmetric(vertical: 30.0),
        child: Center(
          child: SizedBox(
            width: 300.0,
            height: 300.0,
            child: ExampleZdcMap(
              initialCameraPosition: _kInitialPosition,
              liteModeEnabled: true,
            ),
          ),
        ),
      ),
    );
  }
}
