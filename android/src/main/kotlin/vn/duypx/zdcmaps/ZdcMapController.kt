//// Copyright 2013 The Flutter Authors. All rights reserved.
//// Use of this source code is governed by a BSD-style license that can be
//// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.zdc.android.zms.maps.CameraUpdate
import com.zdc.android.zms.maps.CameraUpdateFactory
import com.zdc.android.zms.maps.MapView
import com.zdc.android.zms.maps.OnMapReadyCallback
import com.zdc.android.zms.maps.ZDCMap
import com.zdc.android.zms.maps.ZDCMapOptions
import com.zdc.android.zms.maps.model.CameraPosition
import com.zdc.android.zms.maps.model.Circle
import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.LatLngBounds
import com.zdc.android.zms.maps.model.Marker
import com.zdc.android.zms.maps.model.Polygon
import com.zdc.android.zms.maps.model.Polyline
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import vn.duypx.zdcmaps.Convert.cameraPositionToJson
import vn.duypx.zdcmaps.Convert.interpretZdcMapOptions
import vn.duypx.zdcmaps.Convert.latLngToJson
import vn.duypx.zdcmaps.Convert.latlngBoundsToJson
import vn.duypx.zdcmaps.Convert.pointToJson
import vn.duypx.zdcmaps.Convert.toCameraUpdate
import vn.duypx.zdcmaps.Convert.toLatLng
import vn.duypx.zdcmaps.Convert.toPoint
import java.io.ByteArrayOutputStream


class ZdcMapController(
    id: Int,
    context: Context,
    binaryMessenger: BinaryMessenger,
    lifecycleProvider: LifecycleProvider,
    options: ZDCMapOptions
) : DefaultLifecycleObserver, ActivityPluginBinding.OnSaveInstanceStateListener, ZdcMapOptionsSink,
    MethodChannel.MethodCallHandler, OnMapReadyCallback, ZdcMapListener, PlatformView {
    private var methodChannel: MethodChannel
    private var lifecycleProvider: LifecycleProvider
    private var context: Context

    private val options: ZDCMapOptions
    private var disposed = false

    private var mapView: MapView
    private var zdcMap: ZDCMap? = null
    private var trackCameraPosition = false
    private var myLocationEnabled = false
    private var myLocationButtonEnabled = false
    private var zoomControlsEnabled = true
    private var indoorEnabled = true
    private var trafficEnabled = false
    private var buildingsEnabled = true

    private var density = 0f

    private var mapReadyResult: MethodChannel.Result? = null

    private val markersController: MarkersController
    private val polygonsController: PolygonsController
    private val polylinesController: PolylinesController
    private val circlesController: CirclesController
    private val tileOverlaysController: TileOverlaysController
    private var initialMarkers: List<Any>? = null
    private var initialPolygons: List<Any>? = null
    private var initialPolylines: List<Any>? = null
    private var initialCircles: List<Any?>? = null
    private var initialTileOverlays: List<Map<String, *>>? = null
    private var initialPadding: MutableList<Int>? = null

    private fun moveCamera(cameraUpdate: CameraUpdate) {
        zdcMap!!.moveCamera(cameraUpdate)
    }

    private fun animateCamera(cameraUpdate: CameraUpdate) {
        zdcMap!!.animateCamera(cameraUpdate)
    }

    private val cameraPosition: CameraPosition?
        get() = if (trackCameraPosition) zdcMap!!.cameraPosition else null
    private var loadedCallbackPending = false

    init {
        this.context = context
        this.options = options
        mapView = MapView(context, options)
        density = context.resources.displayMetrics.density
        methodChannel = MethodChannel(
            binaryMessenger, "vn.duypx/zdc_maps_android_$id"
        )
        methodChannel.setMethodCallHandler(this)
        this.lifecycleProvider = lifecycleProvider
        markersController = MarkersController(methodChannel, context)
        polygonsController = PolygonsController(methodChannel, density)
        polylinesController = PolylinesController(methodChannel, density)
        circlesController = CirclesController(methodChannel, density)
        tileOverlaysController = TileOverlaysController(methodChannel)

        lifecycleProvider.getLifecycle()?.addObserver(this)
        mapView.getMapAsync(this)
    }

    private fun invalidateMapIfNeeded() {
        if (zdcMap == null || loadedCallbackPending) {
            return
        }
        loadedCallbackPending = true
        zdcMap!!.setOnMapLoadedCallback {
            loadedCallbackPending = false
            postFrameCallback {
                postFrameCallback {
                    mapView.invalidate()
                }
            }
        }
    }

    override fun onMapReady(zdcMap: ZDCMap) {
        this.zdcMap = zdcMap
        zdcMap.setOnInfoWindowClickListener(this)
        if (mapReadyResult != null) {
            mapReadyResult!!.success(null)
            mapReadyResult = null
        }
        setZdcMapListener(this)
        updateMyLocationSettings()
        markersController.setZdcMap(zdcMap)
        polygonsController.setZdcMap(zdcMap)
        polylinesController.setZdcMap(zdcMap)
        circlesController.setZdcMap(zdcMap)
        tileOverlaysController.setZdcMap(zdcMap)
        updateInitialMarkers()
        updateInitialPolygons()
        updateInitialPolylines()
        updateInitialCircles()
        updateInitialTileOverlays()
        if (initialPadding != null && initialPadding!!.size == 4) {
            setPadding(
                initialPadding!![0], initialPadding!![1], initialPadding!![2], initialPadding!![3]
            )
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "map#waitForMap" -> {
                if (zdcMap != null) {
                    result.success(null)
                    return
                }
                mapReadyResult = result
            }

            "map#update" -> {
                interpretZdcMapOptions(call.argument("options")!!, this)
                result.success(
                    cameraPositionToJson(
                        cameraPosition
                    )
                )
            }

            "map#getVisibleRegion" -> {
                if (zdcMap != null) {
                    val latLngBounds: LatLngBounds = zdcMap!!.projection.visibleRegion.latLngBounds
                    result.success(latlngBoundsToJson(latLngBounds))
                } else {
                    result.error(
                        "ZdcMap uninitialized",
                        "getVisibleRegion called prior to map initialization",
                        null
                    )
                }
            }

            "map#getScreenCoordinate" -> {
                if (zdcMap != null) {
                    val latLng: LatLng = toLatLng(call.arguments)
                    val screenLocation: Point = zdcMap!!.projection.toScreenLocation(latLng)
                    result.success(pointToJson(screenLocation))
                } else {
                    result.error(
                        "ZdcMap uninitialized",
                        "getScreenCoordinate called prior to map initialization",
                        null
                    )
                }
            }

            "map#getLatLng" -> {
                if (zdcMap != null) {
                    val point: Point = toPoint(call.arguments)
                    val latLng: LatLng = zdcMap!!.projection.fromScreenLocation(point)
                    result.success(latLngToJson(latLng))
                } else {
                    result.error(
                        "ZdcMap uninitialized",
                        "getLatLng called prior to map initialization",
                        null
                    )
                }
            }

            "map#takeSnapshot" -> {
                if (zdcMap != null) {
                    zdcMap!!.snapshot(
                        { bitmap ->
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val byteArray = stream.toByteArray()
                            bitmap.recycle()
                            result.success(byteArray)
                        }, true
                    )
                } else {
                    result.error("ZdcMap uninitialized", "takeSnapshot", null)
                }
            }

            "camera#move" -> {
                val cameraUpdate: CameraUpdate =
                    toCameraUpdate(call.argument("cameraUpdate")!!, density)
                moveCamera(cameraUpdate)
                result.success(null)
            }

            "camera#animate" -> {
                val cameraUpdate: CameraUpdate =
                    toCameraUpdate(call.argument("cameraUpdate")!!, density)
                animateCamera(cameraUpdate)
                result.success(null)
            }

            "markers#update" -> {
                invalidateMapIfNeeded()
                val markersToAdd: List<Any> = call.argument("markersToAdd")!!
                markersController.addMarkers(markersToAdd)
                val markersToChange: List<Any> = call.argument("markersToChange")!!
                markersController.changeMarkers(markersToChange)
                val markerIdsToRemove: List<Any> = call.argument("markerIdsToRemove")!!
                markersController.removeMarkers(markerIdsToRemove)
                result.success(null)
            }

            "markers#showInfoWindow" -> {
                val markerId: Any = call.argument("markerId")!!
                markersController.showMarkerInfoWindow(markerId as String, result)
            }

            "markers#hideInfoWindow" -> {
                val markerId: Any = call.argument("markerId")!!
                markersController.hideMarkerInfoWindow(markerId as String, result)
            }

            "markers#isInfoWindowShown" -> {
                val markerId: Any = call.argument("markerId")!!
                markersController.isInfoWindowShown(markerId as String, result)
            }

            "polygons#update" -> {
                invalidateMapIfNeeded()
                val polygonsToAdd: List<Any> = call.argument("polygonsToAdd")!!
                polygonsController.addPolygons(polygonsToAdd)
                val polygonsToChange: List<Any> = call.argument("polygonsToChange")!!
                polygonsController.changePolygons(polygonsToChange)
                val polygonIdsToRemove: List<Any> = call.argument("polygonIdsToRemove")!!
                polygonsController.removePolygons(polygonIdsToRemove)
                result.success(null)
            }

            "polylines#update" -> {
                invalidateMapIfNeeded()
                val polylinesToAdd: List<Any> = call.argument("polylinesToAdd")!!
                polylinesController.addPolylines(polylinesToAdd)
                val polylinesToChange: List<Any> = call.argument("polylinesToChange")!!
                polylinesController.changePolylines(polylinesToChange)
                val polylineIdsToRemove: List<Any> = call.argument("polylineIdsToRemove")!!
                polylinesController.removePolylines(polylineIdsToRemove)
                result.success(null)
            }

            "circles#update" -> {
                invalidateMapIfNeeded()
                val circlesToAdd: List<Any?> = call.argument("circlesToAdd")!!
                circlesController.addCircles(circlesToAdd)
                val circlesToChange: List<Any?> = call.argument("circlesToChange")!!
                circlesController.changeCircles(circlesToChange)
                val circleIdsToRemove: List<Any?> = call.argument("circleIdsToRemove")!!
                circlesController.removeCircles(circleIdsToRemove)
                result.success(null)
            }

            "map#isCompassEnabled" -> {
                result.success(zdcMap!!.uiSettings.isCompassEnabled)
            }

//            "map#isMapToolbarEnabled" -> {
//                result.success(zdcMap!!.getUiSettings().isMapToolbarEnabled())
//            }

            "map#getMinMaxZoomLevels" -> {
                val zoomLevels: MutableList<Double> = ArrayList(2)
                zoomLevels.add(zdcMap!!.minZoomLevel)
                zoomLevels.add(zdcMap!!.maxZoomLevel)
                result.success(zoomLevels)
            }

            "map#isZoomGesturesEnabled" -> {
                result.success(zdcMap!!.uiSettings.isZoomGesturesEnabled)
            }

//            "map#isLiteModeEnabled" -> {
//                result.success(options.getLiteMode())
//            }

            "map#isZoomControlsEnabled" -> {
                result.success(zdcMap!!.uiSettings.isZoomControlsEnabled)
            }

            "map#isScrollGesturesEnabled" -> {
                result.success(zdcMap!!.uiSettings.isScrollGesturesEnabled)
            }

            "map#isTiltGesturesEnabled" -> {
                result.success(zdcMap!!.uiSettings.isTiltGesturesEnabled)
            }

            "map#isRotateGesturesEnabled" -> {
                result.success(zdcMap!!.uiSettings.isRotateGesturesEnabled)
            }

            "map#isMyLocationButtonEnabled" -> {
                result.success(zdcMap!!.uiSettings.isMyLocationButtonEnabled)
            }

//            "map#isTrafficEnabled" -> {
//                result.success(zdcMap!!.isTrafficEnabled())
//            }

//            "map#isBuildingsEnabled" -> {
//                result.success(zdcMap!!.isBuildingsEnabled())
//            }

            "map#getZoomLevel" -> {
                result.success(zdcMap!!.cameraPosition.zoom)
            }

//            "map#setStyle" -> {
//                invalidateMapIfNeeded()
//                val mapStyleSet: Boolean
//                mapStyleSet = if (call.arguments is String) {
//                    val mapStyle = call.arguments as String
//                    if (mapStyle == null) {
//                        zdcMap!!.setMapStyle(null)
//                    } else {
//                        zdcMap!!.setMapStyle(MapStyleOptions(mapStyle))
//                    }
//                } else {
//                    zdcMap!!.setMapStyle(null)
//                }
//                val mapStyleResult = ArrayList<Any>(2)
//                mapStyleResult.add(mapStyleSet)
//                if (!mapStyleSet) {
//                    mapStyleResult.add(
//                        "Unable to set the map style. Please check console logs for errors."
//                    )
//                }
//                result.success(mapStyleResult)
//            }

            "tileOverlays#update" -> {
                invalidateMapIfNeeded()
                val tileOverlaysToAdd: List<Map<String, *>> = call.argument("tileOverlaysToAdd")!!
                tileOverlaysController.addTileOverlays(tileOverlaysToAdd)
                val tileOverlaysToChange: List<Map<String, *>> =
                    call.argument("tileOverlaysToChange")!!
                tileOverlaysController.changeTileOverlays(tileOverlaysToChange)
                val tileOverlaysToRemove: List<String> = call.argument("tileOverlayIdsToRemove")!!
                tileOverlaysController.removeTileOverlays(tileOverlaysToRemove)
                result.success(null)
            }

            "tileOverlays#clearTileCache" -> {
                invalidateMapIfNeeded()
                val tileOverlayId: String = call.argument("tileOverlayId")!!
                tileOverlaysController.clearTileCache(tileOverlayId)
                result.success(null)
            }

            "map#getTileOverlayInfo" -> {
                val tileOverlayId: String = call.argument("tileOverlayId")!!
                result.success(tileOverlaysController.getTileOverlayInfo(tileOverlayId))
            }

            else -> result.notImplemented()
        }
    }

    override fun onMapClick(latLng: LatLng?) {
        val arguments: MutableMap<String, Any> = HashMap(2)
        arguments["position"] = latLngToJson(latLng!!)
        methodChannel.invokeMethod("map#onTap", arguments)
    }

    override fun onMapLongClick(latLng: LatLng?) {
        val arguments: MutableMap<String, Any> = HashMap(2)
        arguments["position"] = latLngToJson(latLng!!)
        methodChannel.invokeMethod("map#onLongPress", arguments)
    }

    override fun onInfoWindowClick(marker: Marker) {
        markersController.onInfoWindowTap(marker.id)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return markersController.onMarkerTap(marker.id)
    }

    override fun onPolygonClick(p0: MutableList<Polygon>?) {
//        polygonsController.onPolygonTap(polygon.id)
    }

    override fun onPolylineClick(p0: MutableList<Polyline>?) {
//        polylinesController.onPolylineTap(polyline.id)
    }

    override fun onCircleClick(p0: MutableList<Circle>?) {
//        circlesController.onCircleTap(circle.id)
    }

    override fun getView(): View {
        return mapView
    }

    override fun dispose() {
        if (disposed) {
            return
        }
        disposed = true
        methodChannel.setMethodCallHandler(null)
        setZdcMapListener(null)
        destroyMapViewIfNecessary()
        val lifecycle: Lifecycle? = lifecycleProvider.getLifecycle()
        lifecycle?.removeObserver(this)
    }

    private fun setZdcMapListener(listener: ZdcMapListener?) {
        if (zdcMap == null) {
            Log.v(TAG, "Controller was disposed before ZdcMap was ready.")
            return
        }
        zdcMap!!.setOnMarkerClickListener(listener)
        zdcMap!!.setOnPolygonClickListener(listener)
        zdcMap!!.setOnPolylineClickListener(listener)
        zdcMap!!.setOnCircleClickListener(listener)
        zdcMap!!.setOnMapClickListener(listener)
        zdcMap!!.setOnMapLongClickListener(listener)
    }

    // DefaultLifecycleObserver
    override fun onCreate(owner: LifecycleOwner) {
        if (disposed) {
            return
        }
        mapView.onCreate(null)
    }

    override fun onStart(owner: LifecycleOwner) {
        if (disposed) {
            return
        }
//        mapView.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        if (disposed) {
            return
        }
        mapView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        if (disposed) {
            return
        }
        mapView.onResume()
    }

    override fun onStop(owner: LifecycleOwner) {
        if (disposed) {
            return
        }
//        mapView.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        if (disposed) {
            return
        }
        destroyMapViewIfNecessary()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        if (disposed) {
            return
        }
        mapView.onSaveInstanceState(bundle)
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        if (disposed) {
            return
        }
        mapView.onCreate(bundle)
    }

    // ZdcMapOptionsSink methods
    override fun setCameraTargetBounds(bounds: LatLngBounds?) {
//        zdcMap!!.setLatLngBoundsForCameraTarget(bounds)
    }

    override fun setCompassEnabled(compassEnabled: Boolean) {
        zdcMap!!.uiSettings.isCompassEnabled = compassEnabled
    }

    override fun setMapToolbarEnabled(setMapToolbarEnabled: Boolean) {
//        zdcMap!!.uiSettings.setMapToolbarEnabled(mapToolbarEnabled)
    }

    override fun setMapType(mapType: String) {
        zdcMap!!.mapType = mapType
    }

    override fun setTrackCameraPosition(trackCameraPosition: Boolean) {
        this.trackCameraPosition = trackCameraPosition
    }

    override fun setRotateGesturesEnabled(rotateGesturesEnabled: Boolean) {
        zdcMap!!.uiSettings.isRotateGesturesEnabled = rotateGesturesEnabled
    }

    override fun setScrollGesturesEnabled(scrollGesturesEnabled: Boolean) {
        zdcMap!!.uiSettings.isScrollGesturesEnabled = scrollGesturesEnabled
    }

    override fun setTiltGesturesEnabled(tiltGesturesEnabled: Boolean) {
        zdcMap!!.uiSettings.isTiltGesturesEnabled = tiltGesturesEnabled
    }

    override fun setMinMaxZoomPreference(min: Float?, max: Float?) {
    }

    override fun setPadding(top: Int, left: Int, bottom: Int, right: Int) {
        if (zdcMap != null) {
            zdcMap!!.setPadding(
                (left * density).toInt(),
                (top * density).toInt(),
                (right * density).toInt(),
                (bottom * density).toInt()
            )
        } else {
            setInitialPadding(top, left, bottom, right)
        }
    }

    private fun setInitialPadding(top: Int, left: Int, bottom: Int, right: Int) {
        if (initialPadding == null) {
            initialPadding = ArrayList()
        } else {
            initialPadding!!.clear()
        }
        initialPadding!!.add(top)
        initialPadding!!.add(left)
        initialPadding!!.add(bottom)
        initialPadding!!.add(right)
    }

    override fun setZoomGesturesEnabled(zoomGesturesEnabled: Boolean) {
        zdcMap!!.uiSettings.isZoomGesturesEnabled = zoomGesturesEnabled
    }

    /** This call will have no effect on already created map  */
    override fun setLiteModeEnabled(liteModeEnabled: Boolean) {
//        options.liteMode(liteModeEnabled)
    }

    override fun setMyLocationEnabled(myLocationEnabled: Boolean) {
        if (this.myLocationEnabled == myLocationEnabled) {
            return
        }
        this.myLocationEnabled = myLocationEnabled
        if (zdcMap != null) {
            updateMyLocationSettings()
        }
    }

    override fun setMyLocationButtonEnabled(myLocationButtonEnabled: Boolean) {
        if (this.myLocationButtonEnabled == myLocationButtonEnabled) {
            return
        }
        this.myLocationButtonEnabled = myLocationButtonEnabled
        if (zdcMap != null) {
            updateMyLocationSettings()
        }
    }

    override fun setZoomControlsEnabled(zoomControlsEnabled: Boolean) {
        if (this.zoomControlsEnabled == zoomControlsEnabled) {
            return
        }
        this.zoomControlsEnabled = zoomControlsEnabled
        if (zdcMap != null) {
            zdcMap!!.uiSettings.isZoomControlsEnabled = zoomControlsEnabled
        }
    }

    override fun setInitialMarkers(initialMarkers: Any?) {
        val markers = initialMarkers as ArrayList<*>?
        this.initialMarkers = if (markers != null) ArrayList(markers) else null
        if (zdcMap != null) {
            updateInitialMarkers()
        }
    }

    private fun updateInitialMarkers() {
        markersController.addMarkers(initialMarkers)
    }

    override fun setInitialPolygons(initialPolygons: Any?) {
        val polygons = initialPolygons as ArrayList<*>?
        this.initialPolygons = if (polygons != null) ArrayList(polygons) else null
        if (zdcMap != null) {
            updateInitialPolygons()
        }
    }

    private fun updateInitialPolygons() {
        polygonsController.addPolygons(initialPolygons)
    }

    override fun setInitialPolylines(initialPolylines: Any?) {
        val polylines = initialPolylines as ArrayList<*>?
        this.initialPolylines = if (polylines != null) ArrayList(polylines) else null
        if (zdcMap != null) {
            updateInitialPolylines()
        }
    }

    private fun updateInitialPolylines() {
        polylinesController.addPolylines(initialPolylines)
    }

    override fun setInitialCircles(initialCircles: Any?) {
        val circles = initialCircles as ArrayList<*>?
        this.initialCircles = if (circles != null) ArrayList(circles) else null
        if (zdcMap != null) {
            updateInitialCircles()
        }
    }

    private fun updateInitialCircles() {
        circlesController.addCircles(initialCircles)
    }

    override fun setInitialTileOverlays(initialTileOverlays: List<Map<String, *>>?) {
        this.initialTileOverlays = initialTileOverlays
        if (zdcMap != null) {
            updateInitialTileOverlays()
        }
    }

    private fun updateInitialTileOverlays() {
        tileOverlaysController.addTileOverlays(initialTileOverlays)
    }

    @SuppressLint("MissingPermission")
    private fun updateMyLocationSettings() {
        if (hasLocationPermission()) {
            // The plugin doesn't add the location permission by default so that apps that don't need
            // the feature won't require the permission.
            // Gradle is doing a static check for missing permission and in some configurations will
            // fail the build if the permission is missing. The following disables the Gradle lint.
            zdcMap!!.setMyLocationEnabled(myLocationEnabled)
            zdcMap!!.uiSettings.isMyLocationButtonEnabled = myLocationButtonEnabled
        } else {
            // TODO(amirh): Make the options update fail.
            // https://github.com/flutter/flutter/issues/24327
            Log.e(TAG, "Cannot enable MyLocation layer as location permissions are not granted")
        }
    }

    private fun hasLocationPermission(): Boolean {
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkSelfPermission(permission: String?): Int {
        requireNotNull(permission) { "permission is null" }
        return context.checkPermission(
            permission, android.os.Process.myPid(), android.os.Process.myUid()
        )
    }

    private fun destroyMapViewIfNecessary() {
        mapView.onDestroy()
    }

    override fun setIndoorEnabled(indoorEnabled: Boolean) {
        this.indoorEnabled = indoorEnabled
    }

    override fun setTrafficEnabled(trafficEnabled: Boolean) {
        this.trafficEnabled = trafficEnabled
        if (zdcMap == null) {
            return
        }
//        zdcMap!!.setTrafficEnabled(trafficEnabled)
    }

    override fun setBuildingsEnabled(buildingsEnabled: Boolean) {
        this.buildingsEnabled = buildingsEnabled
    }

    companion object {
        private const val TAG = "ZdcMapController"

        private fun postFrameCallback(f: Runnable) {
            Choreographer.getInstance().postFrameCallback { f.run() }
        }
    }
}
