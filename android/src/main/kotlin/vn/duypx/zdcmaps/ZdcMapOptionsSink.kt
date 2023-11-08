// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLngBounds

/** Receiver of ZdcMap configuration options.  */
interface ZdcMapOptionsSink {
    fun setCameraTargetBounds(bounds: LatLngBounds?)
    fun setCompassEnabled(compassEnabled: Boolean)
    fun setMapToolbarEnabled(setMapToolbarEnabled: Boolean)
    fun setMapType(mapType: String)
    fun setMinMaxZoomPreference(min: Float?, max: Float?)
    fun setPadding(top: Int, left: Int, bottom: Int, right: Int)
    fun setRotateGesturesEnabled(rotateGesturesEnabled: Boolean)
    fun setScrollGesturesEnabled(scrollGesturesEnabled: Boolean)
    fun setTiltGesturesEnabled(tiltGesturesEnabled: Boolean)
    fun setTrackCameraPosition(trackCameraPosition: Boolean)
    fun setZoomGesturesEnabled(zoomGesturesEnabled: Boolean)
    fun setLiteModeEnabled(liteModeEnabled: Boolean)
    fun setMyLocationEnabled(myLocationEnabled: Boolean)
    fun setZoomControlsEnabled(zoomControlsEnabled: Boolean)
    fun setMyLocationButtonEnabled(myLocationButtonEnabled: Boolean)
    fun setIndoorEnabled(indoorEnabled: Boolean)
    fun setTrafficEnabled(trafficEnabled: Boolean)
    fun setBuildingsEnabled(buildingsEnabled: Boolean)
    fun setInitialMarkers(initialMarkers: Any?)
    fun setInitialPolygons(initialPolygons: Any?)
    fun setInitialPolylines(initialPolylines: Any?)
    fun setInitialCircles(initialCircles: Any?)
    fun setInitialTileOverlays(initialTileOverlays: List<Map<String, *>>?)
}
