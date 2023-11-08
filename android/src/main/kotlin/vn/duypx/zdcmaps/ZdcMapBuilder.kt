// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.content.Context
import android.graphics.Rect
import com.zdc.android.zms.maps.ZDCMapOptions
import com.zdc.android.zms.maps.model.CameraPosition
import com.zdc.android.zms.maps.model.LatLngBounds
import io.flutter.plugin.common.BinaryMessenger

class ZdcMapBuilder : ZdcMapOptionsSink {
    private val options: ZDCMapOptions = ZDCMapOptions()
    private var trackCameraPosition = false
    private var myLocationEnabled = false
    private var myLocationButtonEnabled = false
    private var indoorEnabled = true
    private var trafficEnabled = false
    private var buildingsEnabled = true
    private var initialMarkers: Any? = null
    private var initialPolygons: Any? = null
    private var initialPolylines: Any? = null
    private var initialCircles: Any? = null
    private var initialTileOverlays: List<Map<String, *>>? = null
    private var padding: Rect = Rect(0, 0, 0, 0)
    fun build(
        id: Int,
        context: Context,
        binaryMessenger: BinaryMessenger,
        lifecycleProvider: LifecycleProvider
    ): ZdcMapController {
        val controller =
            ZdcMapController(id, context, binaryMessenger, lifecycleProvider, options)
        controller.setMyLocationEnabled(myLocationEnabled)
        controller.setMyLocationButtonEnabled(myLocationButtonEnabled)
        controller.setIndoorEnabled(indoorEnabled)
        controller.setTrafficEnabled(trafficEnabled)
        controller.setBuildingsEnabled(buildingsEnabled)
        controller.setTrackCameraPosition(trackCameraPosition)
        controller.setInitialMarkers(initialMarkers)
        controller.setInitialPolygons(initialPolygons)
        controller.setInitialPolylines(initialPolylines)
        controller.setInitialCircles(initialCircles)
        controller.setPadding(padding.top, padding.left, padding.bottom, padding.right)
        controller.setInitialTileOverlays(initialTileOverlays)
        return controller
    }

    fun setInitialCameraPosition(position: CameraPosition?) {
        options.camera(position)
    }

    fun setMapId(mapId: String?) {
//        options.mapId(mapId)
    }

    override fun setCompassEnabled(compassEnabled: Boolean) {
        options.compassEnabled(compassEnabled)
    }

    override fun setMapToolbarEnabled(setMapToolbarEnabled: Boolean) {
//        options.mapToolbarEnabled(setMapToolbarEnabled)
    }

    override fun setCameraTargetBounds(bounds: LatLngBounds?) {
//        options.latLngBoundsForCameraTarget(bounds)
    }

    override fun setMapType(mapType: String) {
        options.mapType(mapType)
    }

    override fun setMinMaxZoomPreference(min: Float?, max: Float?) {
        if (min != null) {
//            options.minZoomPreference(min)
        }
        if (max != null) {
//            options.maxZoomPreference(max)
        }
    }

    override fun setPadding(top: Int, left: Int, bottom: Int, right: Int) {
        padding = Rect(left, top, right, bottom)
    }

    override fun setTrackCameraPosition(trackCameraPosition: Boolean) {
        this.trackCameraPosition = trackCameraPosition
    }

    override fun setRotateGesturesEnabled(rotateGesturesEnabled: Boolean) {
        options.rotateGesturesEnabled(rotateGesturesEnabled)
    }

    override fun setScrollGesturesEnabled(scrollGesturesEnabled: Boolean) {
        options.scrollGesturesEnabled(scrollGesturesEnabled)
    }

    override fun setTiltGesturesEnabled(tiltGesturesEnabled: Boolean) {
        options.tiltGesturesEnabled(tiltGesturesEnabled)
    }

    override fun setZoomGesturesEnabled(zoomGesturesEnabled: Boolean) {
        options.zoomGesturesEnabled(zoomGesturesEnabled)
    }

    override fun setLiteModeEnabled(liteModeEnabled: Boolean) {
//        options.liteMode(liteModeEnabled)
    }

    override fun setIndoorEnabled(indoorEnabled: Boolean) {
        this.indoorEnabled = indoorEnabled
    }

    override fun setTrafficEnabled(trafficEnabled: Boolean) {
        this.trafficEnabled = trafficEnabled
    }

    override fun setBuildingsEnabled(buildingsEnabled: Boolean) {
        this.buildingsEnabled = buildingsEnabled
    }

    override fun setMyLocationEnabled(myLocationEnabled: Boolean) {
        this.myLocationEnabled = myLocationEnabled
    }

    override fun setZoomControlsEnabled(zoomControlsEnabled: Boolean) {
        options.zoomControlsEnabled(zoomControlsEnabled)
    }

    override fun setMyLocationButtonEnabled(myLocationButtonEnabled: Boolean) {
        this.myLocationButtonEnabled = myLocationButtonEnabled
    }

    override fun setInitialMarkers(initialMarkers: Any?) {
        this.initialMarkers = initialMarkers
    }

    override fun setInitialPolygons(initialPolygons: Any?) {
        this.initialPolygons = initialPolygons
    }

    override fun setInitialPolylines(initialPolylines: Any?) {
        this.initialPolylines = initialPolylines
    }

    override fun setInitialCircles(initialCircles: Any?) {
        this.initialCircles = initialCircles
    }


    override fun setInitialTileOverlays(initialTileOverlays: List<Map<String, *>>?) {
        this.initialTileOverlays = initialTileOverlays
    }
}
