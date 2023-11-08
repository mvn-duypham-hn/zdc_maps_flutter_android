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

class MapClickPage extends ZdcMapExampleAppPage {
  const MapClickPage({Key? key})
      : super(const Icon(Icons.mouse), 'Map click', key: key);

  @override
  Widget build(BuildContext context) {
    return const _MapClickBody();
  }
}

class _MapClickBody extends StatefulWidget {
  const _MapClickBody();

  @override
  State<StatefulWidget> createState() => _MapClickBodyState();
}

class _MapClickBodyState extends State<_MapClickBody> {
  _MapClickBodyState();

  ExampleZdcMapController? mapController;
  LatLng? _lastTap;
  LatLng? _lastLongPress;

  @override
  Widget build(BuildContext context) {
    final ExampleZdcMap zdcMap = ExampleZdcMap(
      onMapCreated: onMapCreated,
      initialCameraPosition: _kInitialPosition,
      onTap: (LatLng pos) {
        setState(() {
          _lastTap = pos;
        });
      },
      onLongPress: (LatLng pos) {
        setState(() {
          _lastLongPress = pos;
        });
      },
    );

    final List<Widget> columnChildren = <Widget>[
      Padding(
        padding: const EdgeInsets.all(10.0),
        child: Center(
          child: SizedBox(
            width: 300.0,
            height: 200.0,
            child: zdcMap,
          ),
        ),
      ),
    ];

    if (mapController != null) {
      final String lastTap = 'Tap:\n${_lastTap ?? ""}\n';
      final String lastLongPress = 'Long press:\n${_lastLongPress ?? ""}';
      columnChildren.add(Center(
          child: Text(
        lastTap,
        textAlign: TextAlign.center,
      )));
      columnChildren.add(Center(
          child: Text(
        _lastTap != null ? 'Tapped' : '',
        textAlign: TextAlign.center,
      )));
      columnChildren.add(Center(
          child: Text(
        lastLongPress,
        textAlign: TextAlign.center,
      )));
      columnChildren.add(Center(
          child: Text(
        _lastLongPress != null ? 'Long pressed' : '',
        textAlign: TextAlign.center,
      )));
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: columnChildren,
    );
  }

  Future<void> onMapCreated(ExampleZdcMapController controller) async {
    setState(() {
      mapController = controller;
    });
  }
}
