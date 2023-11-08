// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:zdc_maps_flutter_platform_interface/zdc_maps_flutter_platform_interface.dart';

// This is a pared down version of the Dart code from the app-facing package,
// to allow running the same examples for package-local testing.
// TODO(stuartmorgan): Consider extracting this to a shared package. See also
// https://github.com/flutter/flutter/issues/46716.

/// Controller for a single ExampleZdcMap instance running on the host platform.
class ExampleZdcMapController {
  ExampleZdcMapController._(
    this._zdcMapState, {
    required this.mapId,
  }) {
    _connectStreams(mapId);
  }

  /// The mapId for this controller
  final int mapId;

  /// Initialize control of a [ExampleZdcMap] with [id].
  ///
  /// Mainly for internal use when instantiating a [ExampleZdcMapController] passed
  /// in [ExampleZdcMap.onMapCreated] callback.
  static Future<ExampleZdcMapController> _init(
    int id,
    CameraPosition initialCameraPosition,
    _ExampleZdcMapState zdcMapState,
  ) async {
    await ZdcMapsFlutterPlatform.instance.init(id);
    return ExampleZdcMapController._(
      zdcMapState,
      mapId: id,
    );
  }

  final _ExampleZdcMapState _zdcMapState;

  void _connectStreams(int mapId) {
    if (_zdcMapState.widget.onCameraMoveStarted != null) {
      ZdcMapsFlutterPlatform.instance
          .onCameraMoveStarted(mapId: mapId)
          .listen((_) => _zdcMapState.widget.onCameraMoveStarted!());
    }
    if (_zdcMapState.widget.onCameraMove != null) {
      ZdcMapsFlutterPlatform.instance.onCameraMove(mapId: mapId).listen(
          (CameraMoveEvent e) => _zdcMapState.widget.onCameraMove!(e.value));
    }
    if (_zdcMapState.widget.onCameraIdle != null) {
      ZdcMapsFlutterPlatform.instance
          .onCameraIdle(mapId: mapId)
          .listen((_) => _zdcMapState.widget.onCameraIdle!());
    }
    ZdcMapsFlutterPlatform.instance
        .onMarkerTap(mapId: mapId)
        .listen((MarkerTapEvent e) => _zdcMapState.onMarkerTap(e.value));
    ZdcMapsFlutterPlatform.instance.onMarkerDragStart(mapId: mapId).listen(
        (MarkerDragStartEvent e) =>
            _zdcMapState.onMarkerDragStart(e.value, e.position));
    ZdcMapsFlutterPlatform.instance.onMarkerDrag(mapId: mapId).listen(
        (MarkerDragEvent e) =>
            _zdcMapState.onMarkerDrag(e.value, e.position));
    ZdcMapsFlutterPlatform.instance.onMarkerDragEnd(mapId: mapId).listen(
        (MarkerDragEndEvent e) =>
            _zdcMapState.onMarkerDragEnd(e.value, e.position));
    ZdcMapsFlutterPlatform.instance.onInfoWindowTap(mapId: mapId).listen(
        (InfoWindowTapEvent e) => _zdcMapState.onInfoWindowTap(e.value));
    ZdcMapsFlutterPlatform.instance
        .onPolylineTap(mapId: mapId)
        .listen((PolylineTapEvent e) => _zdcMapState.onPolylineTap(e.value));
    ZdcMapsFlutterPlatform.instance
        .onPolygonTap(mapId: mapId)
        .listen((PolygonTapEvent e) => _zdcMapState.onPolygonTap(e.value));
    ZdcMapsFlutterPlatform.instance
        .onCircleTap(mapId: mapId)
        .listen((CircleTapEvent e) => _zdcMapState.onCircleTap(e.value));
    ZdcMapsFlutterPlatform.instance
        .onTap(mapId: mapId)
        .listen((MapTapEvent e) => _zdcMapState.onTap(e.position));
    ZdcMapsFlutterPlatform.instance.onLongPress(mapId: mapId).listen(
        (MapLongPressEvent e) => _zdcMapState.onLongPress(e.position));
  }

  /// Updates configuration options of the map user interface.
  Future<void> _updateMapConfiguration(MapConfiguration update) {
    return ZdcMapsFlutterPlatform.instance
        .updateMapConfiguration(update, mapId: mapId);
  }

  /// Updates marker configuration.
  Future<void> _updateMarkers(MarkerUpdates markerUpdates) {
    return ZdcMapsFlutterPlatform.instance
        .updateMarkers(markerUpdates, mapId: mapId);
  }

  /// Updates polygon configuration.
  Future<void> _updatePolygons(PolygonUpdates polygonUpdates) {
    return ZdcMapsFlutterPlatform.instance
        .updatePolygons(polygonUpdates, mapId: mapId);
  }

  /// Updates polyline configuration.
  Future<void> _updatePolylines(PolylineUpdates polylineUpdates) {
    return ZdcMapsFlutterPlatform.instance
        .updatePolylines(polylineUpdates, mapId: mapId);
  }

  /// Updates circle configuration.
  Future<void> _updateCircles(CircleUpdates circleUpdates) {
    return ZdcMapsFlutterPlatform.instance
        .updateCircles(circleUpdates, mapId: mapId);
  }

  /// Updates tile overlays configuration.
  Future<void> _updateTileOverlays(Set<TileOverlay> newTileOverlays) {
    return ZdcMapsFlutterPlatform.instance
        .updateTileOverlays(newTileOverlays: newTileOverlays, mapId: mapId);
  }

  /// Clears the tile cache so that all tiles will be requested again from the
  /// [TileProvider].
  Future<void> clearTileCache(TileOverlayId tileOverlayId) async {
    return ZdcMapsFlutterPlatform.instance
        .clearTileCache(tileOverlayId, mapId: mapId);
  }

  /// Starts an animated change of the map camera position.
  Future<void> animateCamera(CameraUpdate cameraUpdate) {
    return ZdcMapsFlutterPlatform.instance
        .animateCamera(cameraUpdate, mapId: mapId);
  }

  /// Changes the map camera position.
  Future<void> moveCamera(CameraUpdate cameraUpdate) {
    return ZdcMapsFlutterPlatform.instance
        .moveCamera(cameraUpdate, mapId: mapId);
  }

  /// Sets the styling of the base map.
  Future<void> setMapStyle(String? mapStyle) {
    return ZdcMapsFlutterPlatform.instance
        .setMapStyle(mapStyle, mapId: mapId);
  }

  /// Return [LatLngBounds] defining the region that is visible in a map.
  Future<LatLngBounds> getVisibleRegion() {
    return ZdcMapsFlutterPlatform.instance.getVisibleRegion(mapId: mapId);
  }

  /// Return [ScreenCoordinate] of the [LatLng] in the current map view.
  Future<ScreenCoordinate> getScreenCoordinate(LatLng latLng) {
    return ZdcMapsFlutterPlatform.instance
        .getScreenCoordinate(latLng, mapId: mapId);
  }

  /// Returns [LatLng] corresponding to the [ScreenCoordinate] in the current map view.
  Future<LatLng> getLatLng(ScreenCoordinate screenCoordinate) {
    return ZdcMapsFlutterPlatform.instance
        .getLatLng(screenCoordinate, mapId: mapId);
  }

  /// Programmatically show the Info Window for a [Marker].
  Future<void> showMarkerInfoWindow(MarkerId markerId) {
    return ZdcMapsFlutterPlatform.instance
        .showMarkerInfoWindow(markerId, mapId: mapId);
  }

  /// Programmatically hide the Info Window for a [Marker].
  Future<void> hideMarkerInfoWindow(MarkerId markerId) {
    return ZdcMapsFlutterPlatform.instance
        .hideMarkerInfoWindow(markerId, mapId: mapId);
  }

  /// Returns `true` when the [InfoWindow] is showing, `false` otherwise.
  Future<bool> isMarkerInfoWindowShown(MarkerId markerId) {
    return ZdcMapsFlutterPlatform.instance
        .isMarkerInfoWindowShown(markerId, mapId: mapId);
  }

  /// Returns the current zoom level of the map
  Future<double> getZoomLevel() {
    return ZdcMapsFlutterPlatform.instance.getZoomLevel(mapId: mapId);
  }

  /// Returns the image bytes of the map
  Future<Uint8List?> takeSnapshot() {
    return ZdcMapsFlutterPlatform.instance.takeSnapshot(mapId: mapId);
  }

  /// Disposes of the platform resources
  void dispose() {
    ZdcMapsFlutterPlatform.instance.dispose(mapId: mapId);
  }
}

// The next map ID to create.
int _nextMapCreationId = 0;

/// A widget which displays a map with data obtained from the Zdc Maps service.
class ExampleZdcMap extends StatefulWidget {
  /// Creates a widget displaying data from Zdc Maps services.
  ///
  /// [AssertionError] will be thrown if [initialCameraPosition] is null;
  const ExampleZdcMap({
    super.key,
    required this.initialCameraPosition,
    this.onMapCreated,
    this.gestureRecognizers = const <Factory<OneSequenceGestureRecognizer>>{},
    this.compassEnabled = true,
    this.mapToolbarEnabled = true,
    this.cameraTargetBounds = CameraTargetBounds.unbounded,
    this.mapType = MapType.normal,
    this.minMaxZoomPreference = MinMaxZoomPreference.unbounded,
    this.rotateGesturesEnabled = true,
    this.scrollGesturesEnabled = true,
    this.zoomControlsEnabled = true,
    this.zoomGesturesEnabled = true,
    this.liteModeEnabled = false,
    this.tiltGesturesEnabled = true,
    this.myLocationEnabled = false,
    this.myLocationButtonEnabled = true,
    this.layoutDirection,

    /// If no padding is specified default padding will be 0.
    this.padding = EdgeInsets.zero,
    this.indoorViewEnabled = false,
    this.trafficEnabled = false,
    this.buildingsEnabled = true,
    this.markers = const <Marker>{},
    this.polygons = const <Polygon>{},
    this.polylines = const <Polyline>{},
    this.circles = const <Circle>{},
    this.onCameraMoveStarted,
    this.tileOverlays = const <TileOverlay>{},
    this.onCameraMove,
    this.onCameraIdle,
    this.onTap,
    this.onLongPress,
    this.cloudMapId,
  });

  /// Callback method for when the map is ready to be used.
  ///
  /// Used to receive a [ExampleZdcMapController] for this [ExampleZdcMap].
  final void Function(ExampleZdcMapController controller)? onMapCreated;

  /// The initial position of the map's camera.
  final CameraPosition initialCameraPosition;

  /// True if the map should show a compass when rotated.
  final bool compassEnabled;

  /// True if the map should show a toolbar when you interact with the map. Android only.
  final bool mapToolbarEnabled;

  /// Geographical bounding box for the camera target.
  final CameraTargetBounds cameraTargetBounds;

  /// Type of map tiles to be rendered.
  final MapType mapType;

  /// The layout direction to use for the embedded view.
  final TextDirection? layoutDirection;

  /// Preferred bounds for the camera zoom level.
  ///
  /// Actual bounds depend on map data and device.
  final MinMaxZoomPreference minMaxZoomPreference;

  /// True if the map view should respond to rotate gestures.
  final bool rotateGesturesEnabled;

  /// True if the map view should respond to scroll gestures.
  final bool scrollGesturesEnabled;

  /// True if the map view should show zoom controls. This includes two buttons
  /// to zoom in and zoom out. The default value is to show zoom controls.
  final bool zoomControlsEnabled;

  /// True if the map view should respond to zoom gestures.
  final bool zoomGesturesEnabled;

  /// True if the map view should be in lite mode. Android only.
  final bool liteModeEnabled;

  /// True if the map view should respond to tilt gestures.
  final bool tiltGesturesEnabled;

  /// Padding to be set on map.
  final EdgeInsets padding;

  /// Markers to be placed on the map.
  final Set<Marker> markers;

  /// Polygons to be placed on the map.
  final Set<Polygon> polygons;

  /// Polylines to be placed on the map.
  final Set<Polyline> polylines;

  /// Circles to be placed on the map.
  final Set<Circle> circles;

  /// Tile overlays to be placed on the map.
  final Set<TileOverlay> tileOverlays;

  /// Called when the camera starts moving.
  final VoidCallback? onCameraMoveStarted;

  /// Called repeatedly as the camera continues to move after an
  /// onCameraMoveStarted call.
  final CameraPositionCallback? onCameraMove;

  /// Called when camera movement has ended, there are no pending
  /// animations and the user has stopped interacting with the map.
  final VoidCallback? onCameraIdle;

  /// Called every time a [ExampleZdcMap] is tapped.
  final ArgumentCallback<LatLng>? onTap;

  /// Called every time a [ExampleZdcMap] is long pressed.
  final ArgumentCallback<LatLng>? onLongPress;

  /// True if a "My Location" layer should be shown on the map.
  final bool myLocationEnabled;

  /// Enables or disables the my-location button.
  final bool myLocationButtonEnabled;

  /// Enables or disables the indoor view from the map
  final bool indoorViewEnabled;

  /// Enables or disables the traffic layer of the map
  final bool trafficEnabled;

  /// Enables or disables showing 3D buildings where available
  final bool buildingsEnabled;

  /// Which gestures should be consumed by the map.
  final Set<Factory<OneSequenceGestureRecognizer>> gestureRecognizers;

  /// Identifier that's associated with a specific cloud-based map style.
  ///
  /// See https://developers.zdc.com/maps/documentation/get-map-id
  /// for more details.
  final String? cloudMapId;

  /// Creates a [State] for this [ExampleZdcMap].
  @override
  State createState() => _ExampleZdcMapState();
}

class _ExampleZdcMapState extends State<ExampleZdcMap> {
  final int _mapId = _nextMapCreationId++;

  final Completer<ExampleZdcMapController> _controller =
      Completer<ExampleZdcMapController>();

  Map<MarkerId, Marker> _markers = <MarkerId, Marker>{};
  Map<PolygonId, Polygon> _polygons = <PolygonId, Polygon>{};
  Map<PolylineId, Polyline> _polylines = <PolylineId, Polyline>{};
  Map<CircleId, Circle> _circles = <CircleId, Circle>{};
  late MapConfiguration _mapConfiguration;

  @override
  Widget build(BuildContext context) {
    return ZdcMapsFlutterPlatform.instance.buildViewWithConfiguration(
      _mapId,
      onPlatformViewCreated,
      widgetConfiguration: MapWidgetConfiguration(
        textDirection: widget.layoutDirection ??
            Directionality.maybeOf(context) ??
            TextDirection.ltr,
        initialCameraPosition: widget.initialCameraPosition,
        gestureRecognizers: widget.gestureRecognizers,
      ),
      mapObjects: MapObjects(
        markers: widget.markers,
        polygons: widget.polygons,
        polylines: widget.polylines,
        circles: widget.circles,
      ),
      mapConfiguration: _mapConfiguration,
    );
  }

  @override
  void initState() {
    super.initState();
    _mapConfiguration = _configurationFromMapWidget(widget);
    _markers = keyByMarkerId(widget.markers);
    _polygons = keyByPolygonId(widget.polygons);
    _polylines = keyByPolylineId(widget.polylines);
    _circles = keyByCircleId(widget.circles);
  }

  @override
  void dispose() {
    _controller.future
        .then((ExampleZdcMapController controller) => controller.dispose());
    super.dispose();
  }

  @override
  void didUpdateWidget(ExampleZdcMap oldWidget) {
    super.didUpdateWidget(oldWidget);
    _updateOptions();
    _updateMarkers();
    _updatePolygons();
    _updatePolylines();
    _updateCircles();
    _updateTileOverlays();
  }

  Future<void> _updateOptions() async {
    final MapConfiguration newConfig = _configurationFromMapWidget(widget);
    final MapConfiguration updates = newConfig.diffFrom(_mapConfiguration);
    if (updates.isEmpty) {
      return;
    }
    final ExampleZdcMapController controller = await _controller.future;
    unawaited(controller._updateMapConfiguration(updates));
    _mapConfiguration = newConfig;
  }

  Future<void> _updateMarkers() async {
    final ExampleZdcMapController controller = await _controller.future;
    unawaited(controller._updateMarkers(
        MarkerUpdates.from(_markers.values.toSet(), widget.markers)));
    _markers = keyByMarkerId(widget.markers);
  }

  Future<void> _updatePolygons() async {
    final ExampleZdcMapController controller = await _controller.future;
    unawaited(controller._updatePolygons(
        PolygonUpdates.from(_polygons.values.toSet(), widget.polygons)));
    _polygons = keyByPolygonId(widget.polygons);
  }

  Future<void> _updatePolylines() async {
    final ExampleZdcMapController controller = await _controller.future;
    unawaited(controller._updatePolylines(
        PolylineUpdates.from(_polylines.values.toSet(), widget.polylines)));
    _polylines = keyByPolylineId(widget.polylines);
  }

  Future<void> _updateCircles() async {
    final ExampleZdcMapController controller = await _controller.future;
    unawaited(controller._updateCircles(
        CircleUpdates.from(_circles.values.toSet(), widget.circles)));
    _circles = keyByCircleId(widget.circles);
  }

  Future<void> _updateTileOverlays() async {
    final ExampleZdcMapController controller = await _controller.future;
    unawaited(controller._updateTileOverlays(widget.tileOverlays));
  }

  Future<void> onPlatformViewCreated(int id) async {
    final ExampleZdcMapController controller =
        await ExampleZdcMapController._init(
      id,
      widget.initialCameraPosition,
      this,
    );
    _controller.complete(controller);
    unawaited(_updateTileOverlays());
    widget.onMapCreated?.call(controller);
  }

  void onMarkerTap(MarkerId markerId) {
    _markers[markerId]!.onTap?.call();
  }

  void onMarkerDragStart(MarkerId markerId, LatLng position) {
    _markers[markerId]!.onDragStart?.call(position);
  }

  void onMarkerDrag(MarkerId markerId, LatLng position) {
    _markers[markerId]!.onDrag?.call(position);
  }

  void onMarkerDragEnd(MarkerId markerId, LatLng position) {
    _markers[markerId]!.onDragEnd?.call(position);
  }

  void onPolygonTap(PolygonId polygonId) {
    _polygons[polygonId]!.onTap?.call();
  }

  void onPolylineTap(PolylineId polylineId) {
    _polylines[polylineId]!.onTap?.call();
  }

  void onCircleTap(CircleId circleId) {
    _circles[circleId]!.onTap?.call();
  }

  void onInfoWindowTap(MarkerId markerId) {
    _markers[markerId]!.infoWindow.onTap?.call();
  }

  void onTap(LatLng position) {
    widget.onTap?.call(position);
  }

  void onLongPress(LatLng position) {
    widget.onLongPress?.call(position);
  }
}

/// Builds a [MapConfiguration] from the given [map].
MapConfiguration _configurationFromMapWidget(ExampleZdcMap map) {
  return MapConfiguration(
    compassEnabled: map.compassEnabled,
    mapToolbarEnabled: map.mapToolbarEnabled,
    cameraTargetBounds: map.cameraTargetBounds,
    mapType: map.mapType,
    minMaxZoomPreference: map.minMaxZoomPreference,
    rotateGesturesEnabled: map.rotateGesturesEnabled,
    scrollGesturesEnabled: map.scrollGesturesEnabled,
    tiltGesturesEnabled: map.tiltGesturesEnabled,
    trackCameraPosition: map.onCameraMove != null,
    zoomControlsEnabled: map.zoomControlsEnabled,
    zoomGesturesEnabled: map.zoomGesturesEnabled,
    liteModeEnabled: map.liteModeEnabled,
    myLocationEnabled: map.myLocationEnabled,
    myLocationButtonEnabled: map.myLocationButtonEnabled,
    padding: map.padding,
    indoorViewEnabled: map.indoorViewEnabled,
    trafficEnabled: map.trafficEnabled,
    buildingsEnabled: map.buildingsEnabled,
    cloudMapId: map.cloudMapId,
  );
}
