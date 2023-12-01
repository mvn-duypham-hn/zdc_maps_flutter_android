// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import com.zdc.android.zms.maps.CameraUpdate
import com.zdc.android.zms.maps.CameraUpdateFactory
import com.zdc.android.zms.maps.model.CameraPosition
import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.LatLngBounds

/** Conversions between JSON-like values and ZdcMaps data types.  */
internal object Convert {

    @SuppressLint("PrivateResource")
    private fun toBitmapDescriptor(o: Any, context: Context): Bitmap {
        val data = toList(o)
        return when (toString(data!![0]!!)) {
            "defaultMarker" -> if (data.size == 1) {
                BitmapFactory.decodeResource(context.resources, R.drawable.marker)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.marker)
            }

            "fromAsset" -> if (data.size == 2) {
                // TODO need update
                BitmapFactory.decodeResource(context.resources, R.drawable.marker)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.marker)
            }
//
            "fromAssetImage" -> if (data.size == 3) {
                // TODO need update
                BitmapFactory.decodeResource(context.resources, R.drawable.marker)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.marker)
                throw IllegalArgumentException(
                    "'fromAssetImage' Expected exactly 3 arguments, got: " + data.size
                )
            }

            "fromBytes" -> getBitmapFromBytes(data)
            else -> throw IllegalArgumentException("Cannot interpret $o as BitmapDescriptor")
        }
    }

    private fun getBitmapFromBytes(data: List<*>?): Bitmap {
        return if (data!!.size == 2) {
            try {
                toBitmap(data[1]!!)
            } catch (e: Exception) {
                throw IllegalArgumentException("Unable to interpret bytes as a valid image.", e)
            }
        } else {
            throw IllegalArgumentException(
                "fromBytes should have exactly one argument, interpretTileOverlayOptions the bytes. Got: "
                        + data.size
            )
        }
    }

    private fun toBoolean(o: Any): Boolean {
        return o as Boolean
    }

    fun toCameraPosition(o: Any): CameraPosition {
        val data = toMap(o)
        val builder: CameraPosition.Builder = CameraPosition.builder()
        builder.bearing(toFloat(data["bearing"]!!))
        builder.target(toLatLng(data["target"]!!))
        builder.tilt(toFloat(data["tilt"]!!))
        builder.zoom(toDouble(data["zoom"]!!))
        return builder.build()
    }

    fun toCameraUpdate(o: Any, density: Float): CameraUpdate {
        val data = toList(o)
        return when (toString(data!![0]!!)) {
            "newCameraPosition" -> CameraUpdateFactory.newCameraPosition(
                toCameraPosition(
                    data[1]!!
                )
            )

            "newLatLng" -> CameraUpdateFactory.newLatLng(
                toLatLng(
                    data[1]
                )
            )

            "newLatLngBounds" -> CameraUpdateFactory.newLatLngBounds(
                toLatLngBounds(data[1]), toPixels(
                    data[2]!!, density
                )
            )

            "newLatLngZoom" -> CameraUpdateFactory.newLatLngZoom(
                toLatLng(
                    data[1]
                ), toDouble(data[2]!!)
            )

            "scrollBy" -> CameraUpdateFactory.scrollBy( //
                toFractionalPixels(data[1]!!, density),  //
                toFractionalPixels(data[2]!!, density)
            )

            "zoomBy" -> if (data.size == 2) {
                CameraUpdateFactory.zoomBy(toDouble(data[1]!!))
            } else {
                CameraUpdateFactory.zoomBy(
                    toDouble(data[1]!!), toPoint(
                        (data[2])!!, density
                    )
                )
            }

            "zoomIn" -> CameraUpdateFactory.zoomIn()
            "zoomOut" -> CameraUpdateFactory.zoomOut()
            "zoomTo" -> CameraUpdateFactory.zoomTo(toDouble(data[1]!!))

            else -> throw IllegalArgumentException("Cannot interpret $o as CameraUpdate")
        }
    }

    private fun toDouble(o: Any): Double {
        return (o as Number).toDouble()
    }

    private fun toFloat(o: Any): Float {
        return (o as Number).toFloat()
    }

    private fun toFloatWrapper(o: Any?): Float? {
        return if (o == null) null else toFloat(o)
    }

    private fun toInt(o: Any): Int {
        return (o as Number).toInt()
    }

    fun cameraPositionToJson(position: CameraPosition?): Any? {
        if (position == null) {
            return null
        }
        val data: MutableMap<String, Any> = HashMap()
        data["bearing"] = position.bearing
        data["target"] = latLngToJson(position.target)
        data["tilt"] = position.tilt
        data["zoom"] = position.zoom
        return data
    }

    fun latlngBoundsToJson(latLngBounds: LatLngBounds): Any {
        val arguments: MutableMap<String, Any> = HashMap(2)
        arguments["southwest"] = latLngToJson(latLngBounds.southwest)
        arguments["northeast"] = latLngToJson(latLngBounds.northeast)
        return arguments
    }

    fun markerIdToJson(markerId: String?): Any? {
        if (markerId == null) {
            return null
        }
        val data: MutableMap<String, Any> = HashMap(1)
        data["markerId"] = markerId
        return data
    }

    fun polygonIdToJson(polygonId: String?): Any? {
        if (polygonId == null) {
            return null
        }
        val data: MutableMap<String, Any> = HashMap(1)
        data["polygonId"] = polygonId
        return data
    }

    fun polylineIdToJson(polylineId: String?): Any? {
        if (polylineId == null) {
            return null
        }
        val data: MutableMap<String, Any> = HashMap(1)
        data["polylineId"] = polylineId
        return data
    }

    fun circleIdToJson(circleId: String?): Any? {
        if (circleId == null) {
            return null
        }
        val data: MutableMap<String, Any> = HashMap(1)
        data["circleId"] = circleId
        return data
    }

    fun tileOverlayArgumentsToJson(
        tileOverlayId: String?, x: Int, y: Int, zoom: Int
    ): Map<String, Any>? {
        if (tileOverlayId == null) {
            return null
        }
        val data: MutableMap<String, Any> = HashMap(4)
        data["tileOverlayId"] = tileOverlayId
        data["x"] = x
        data["y"] = y
        data["zoom"] = zoom
        return data
    }

    fun latLngToJson(latLng: LatLng): Any {
        return listOf<Any>(latLng.latitude, latLng.longitude)
    }

    fun toLatLng(o: Any?): LatLng {
        val data = toList(o)
        return LatLng(
            toDouble(data!![0]!!), toDouble(
                data[1]!!
            )
        )
    }

    fun toPoint(o: Any): Point {
        val x = toMap(o)["x"]
        val y = toMap(o)["y"]
        return Point(x as Int, y as Int)
    }

    fun pointToJson(point: Point): Map<String, Int> {
        val data: MutableMap<String, Int> = HashMap(2)
        data["x"] = point.x
        data["y"] = point.y
        return data
    }

    private fun toLatLngBounds(o: Any?): LatLngBounds? {
        if (o == null) {
            return null
        }
        val data = toList(o)
        return LatLngBounds(
            toLatLng(data!![0]), toLatLng(
                data[1]
            )
        )
    }

    private fun toList(o: Any?): List<*>? {
        return o as List<*>?
    }

    private fun toMap(o: Any): Map<*, *> {
        return o as Map<*, *>
    }

    private fun toObjectMap(o: Any): Map<String, Any> {
        val hashMap: MutableMap<String, Any> = HashMap()
        val map = o as Map<*, *>
        for (key in map.keys) {
            val `object` = map[key]
            if (`object` != null) {
                hashMap[key as String] = `object`
            }
        }
        return hashMap
    }

    private fun toFractionalPixels(o: Any, density: Float): Float {
        return toFloat(o) * density
    }

    private fun toPixels(o: Any, density: Float): Int {
        return toFractionalPixels(o, density).toInt()
    }

    private fun toBitmap(o: Any): Bitmap {
        val bmpData = o as ByteArray
        return BitmapFactory.decodeByteArray(bmpData, 0, bmpData.size)
    }

    private fun toPoint(o: Any, density: Float): Point {
        val data = toList(o)
        return Point(
            toPixels(data!![0]!!, density), toPixels(
                data[1]!!, density
            )
        )
    }

    private fun toString(o: Any): String {
        return o as String
    }

    private fun toMapType(o: Any): String {
        return when (o as Int) {
            1 -> "its-mo"
            2 -> ""
            else -> "its-mo"
        }
    }

    fun interpretZdcMapOptions(o: Any, sink: ZdcMapOptionsSink) {
        val data = toMap(o)
        val cameraTargetBounds = data["cameraTargetBounds"]
        if (cameraTargetBounds != null) {
            val targetData = toList(cameraTargetBounds)
            sink.setCameraTargetBounds(
                toLatLngBounds(
                    targetData!![0]
                )
            )
        }
        val compassEnabled = data["compassEnabled"]
        if (compassEnabled != null) {
            sink.setCompassEnabled(toBoolean(compassEnabled))
        }
        val mapToolbarEnabled = data["mapToolbarEnabled"]
        if (mapToolbarEnabled != null) {
            sink.setMapToolbarEnabled(toBoolean(mapToolbarEnabled))
        }
        val mapType = data["mapType"]
        if (mapType != null) {
            sink.setMapType(toMapType(mapType))
        }
        val minMaxZoomPreference = data["minMaxZoomPreference"]
        if (minMaxZoomPreference != null) {
            val zoomPreferenceData = toList(minMaxZoomPreference)
            sink.setMinMaxZoomPreference( //
                toFloatWrapper(zoomPreferenceData!![0]),  //
                toFloatWrapper(zoomPreferenceData[1])
            )
        }
        val padding = data["padding"]
        if (padding != null) {
            val paddingData = toList(padding)
            sink.setPadding(
                toInt(paddingData!![0]!!),
                toInt(paddingData[1]!!),
                toInt(paddingData[2]!!),
                toInt(paddingData[3]!!)
            )
        }
        val rotateGesturesEnabled = data["rotateGesturesEnabled"]
        if (rotateGesturesEnabled != null) {
            sink.setRotateGesturesEnabled(toBoolean(rotateGesturesEnabled))
        }
        val scrollGesturesEnabled = data["scrollGesturesEnabled"]
        if (scrollGesturesEnabled != null) {
            sink.setScrollGesturesEnabled(toBoolean(scrollGesturesEnabled))
        }
        val tiltGesturesEnabled = data["tiltGesturesEnabled"]
        if (tiltGesturesEnabled != null) {
            sink.setTiltGesturesEnabled(toBoolean(tiltGesturesEnabled))
        }
        val trackCameraPosition = data["trackCameraPosition"]
        if (trackCameraPosition != null) {
            sink.setTrackCameraPosition(toBoolean(trackCameraPosition))
        }
        val zoomGesturesEnabled = data["zoomGesturesEnabled"]
        if (zoomGesturesEnabled != null) {
            sink.setZoomGesturesEnabled(toBoolean(zoomGesturesEnabled))
        }
        val liteModeEnabled = data["liteModeEnabled"]
        if (liteModeEnabled != null) {
            sink.setLiteModeEnabled(toBoolean(liteModeEnabled))
        }
        val myLocationEnabled = data["myLocationEnabled"]
        if (myLocationEnabled != null) {
            sink.setMyLocationEnabled(toBoolean(myLocationEnabled))
        }
        val zoomControlsEnabled = data["zoomControlsEnabled"]
        if (zoomControlsEnabled != null) {
            sink.setZoomControlsEnabled(toBoolean(zoomControlsEnabled))
        }
        val myLocationButtonEnabled = data["myLocationButtonEnabled"]
        if (myLocationButtonEnabled != null) {
            sink.setMyLocationButtonEnabled(toBoolean(myLocationButtonEnabled))
        }
        val indoorEnabled = data["indoorEnabled"]
        if (indoorEnabled != null) {
            sink.setIndoorEnabled(toBoolean(indoorEnabled))
        }
        val trafficEnabled = data["trafficEnabled"]
        if (trafficEnabled != null) {
            sink.setTrafficEnabled(toBoolean(trafficEnabled))
        }
        val buildingsEnabled = data["buildingsEnabled"]
        if (buildingsEnabled != null) {
            sink.setBuildingsEnabled(toBoolean(buildingsEnabled))
        }
    }

    /** Returns the dartMarkerId of the interpreted marker.  */
    fun interpretMarkerOptions(o: Any, sink: MarkerOptionsSink, context: Context): String {
        val data = toMap(o)
        val alpha = data["alpha"]
        if (alpha != null) {
            sink.setAlpha(toFloat(alpha))
        }
        val anchor = data["anchor"]
        if (anchor != null) {
            val anchorData = toList(anchor)
            sink.setAnchor(
                toFloat(anchorData!![0]!!), toFloat(
                    anchorData[1]!!
                )
            )
        }
        val consumeTapEvents = data["consumeTapEvents"]
        if (consumeTapEvents != null) {
            sink.setConsumeTapEvents(toBoolean(consumeTapEvents))
        }
        val draggable = data["draggable"]
        if (draggable != null) {
            sink.setDraggable(toBoolean(draggable))
        }
        val flat = data["flat"]
        if (flat != null) {
            sink.setFlat(toBoolean(flat))
        }
        val icon = data["icon"]
        if (icon != null) {
            sink.setIcon(toBitmapDescriptor(icon, context))
//            sink.setIcon(toBitmap(icon))
        }
        val infoWindow = data["infoWindow"]
        if (infoWindow != null) {
            interpretInfoWindowOptions(sink, toObjectMap(infoWindow))
        }
        val position = data["position"]
        if (position != null) {
            sink.setPosition(toLatLng(position))
        }
        val rotation = data["rotation"]
        if (rotation != null) {
            sink.setRotation(toFloat(rotation))
        }
        val visible = data["visible"]
        if (visible != null) {
            sink.setVisible(toBoolean(visible))
        }
        val zIndex = data["zIndex"]
        if (zIndex != null) {
            sink.setZIndex(toFloat(zIndex))
        }
        val markerId = data["markerId"] as String?
        return markerId ?: throw IllegalArgumentException("markerId was null")
    }

    private fun interpretInfoWindowOptions(
        sink: MarkerOptionsSink, infoWindow: Map<String, Any>
    ) {
        val title = infoWindow["title"] as String?
        val snippet = infoWindow["snippet"] as String?
        // snippet is nullable.
        if (title != null) {
            sink.setInfoWindowText(title, snippet)
        }
        val infoWindowAnchor = infoWindow["anchor"]
        if (infoWindowAnchor != null) {
            val anchorData = toList(infoWindowAnchor)
            sink.setInfoWindowAnchor(
                toFloat(anchorData!![0]!!), toFloat(
                    anchorData[1]!!
                )
            )
        }
    }

    fun interpretPolygonOptions(o: Any, sink: PolygonOptionsSink): String {
        val data = toMap(o)
        val consumeTapEvents = data["consumeTapEvents"]
        if (consumeTapEvents != null) {
            sink.setConsumeTapEvents(toBoolean(consumeTapEvents))
        }
        val geodesic = data["geodesic"]
        if (geodesic != null) {
            sink.setGeodesic(toBoolean(geodesic))
        }
        val visible = data["visible"]
        if (visible != null) {
            sink.setVisible(toBoolean(visible))
        }
        val fillColor = data["fillColor"]
        if (fillColor != null) {
            sink.setFillColor(toInt(fillColor))
        }
        val strokeColor = data["strokeColor"]
        if (strokeColor != null) {
            sink.setStrokeColor(toInt(strokeColor))
        }
        val strokeWidth = data["strokeWidth"]
        if (strokeWidth != null) {
            sink.setStrokeWidth(toFloat(strokeWidth))
        }
        val zIndex = data["zIndex"]
        if (zIndex != null) {
            sink.setZIndex(toInt(zIndex))
        }
        val points = data["points"]
        if (points != null) {
            sink.setPoints(toPoints(points))
        }
        val holes = data["holes"]
        if (holes != null) {
            sink.setHoles(toHoles(holes))
        }
        val polygonId = data["polygonId"] as String?
        return polygonId ?: throw IllegalArgumentException("polygonId was null")
    }

    fun interpretPolylineOptions(o: Any, sink: PolylineOptionsSink): String {
        val data = toMap(o)
        val consumeTapEvents = data["consumeTapEvents"]
        if (consumeTapEvents != null) {
            sink.setConsumeTapEvents(toBoolean(consumeTapEvents))
        }
        val color = data["color"]
        if (color != null) {
            sink.setColor(toInt(color))
        }
        val geodesic = data["geodesic"]
        if (geodesic != null) {
            sink.setGeodesic(toBoolean(geodesic))
        }
        val jointType = data["jointType"]
        if (jointType != null) {
            sink.setJointType(toInt(jointType))
        }
        val visible = data["visible"]
        if (visible != null) {
            sink.setVisible(toBoolean(visible))
        }
        val width = data["width"]
        if (width != null) {
            sink.setWidth(toFloat(width))
        }
        val zIndex = data["zIndex"]
        if (zIndex != null) {
            sink.setZIndex(toInt(zIndex))
        }
        val points = data["points"]
        if (points != null) {
            sink.setPoints(toPoints(points))
        }
        val polylineId = data["polylineId"] as String?
        return polylineId ?: throw IllegalArgumentException("polylineId was null")
    }

    fun interpretCircleOptions(o: Any, sink: CircleOptionsSink): String {
        val data = toMap(o)
        val consumeTapEvents = data["consumeTapEvents"]
        if (consumeTapEvents != null) {
            sink.setConsumeTapEvents(toBoolean(consumeTapEvents))
        }
        val fillColor = data["fillColor"]
        if (fillColor != null) {
            sink.setFillColor(toInt(fillColor))
        }
        val strokeColor = data["strokeColor"]
        if (strokeColor != null) {
            sink.setStrokeColor(toInt(strokeColor))
        }
        val visible = data["visible"]
        if (visible != null) {
            sink.setVisible(toBoolean(visible))
        }
        val strokeWidth = data["strokeWidth"]
        if (strokeWidth != null) {
            sink.setStrokeWidth(toInt(strokeWidth).toFloat())
        }
        val zIndex = data["zIndex"]
        if (zIndex != null) {
            sink.setZIndex(toInt(zIndex))
        }
        val center = data["center"]
        if (center != null) {
            sink.setCenter(toLatLng(center))
        }
        val radius = data["radius"]
        if (radius != null) {
            sink.setRadius(toDouble(radius))
        }
        val circleId = data["circleId"] as String?
        return circleId ?: throw IllegalArgumentException("circleId was null")
    }

    private fun toPoints(o: Any?): List<LatLng> {
        val data = toList(o)
        val points: MutableList<LatLng> = ArrayList(data!!.size)
        for (rawPoint in data) {
            val point = toList(rawPoint)
            points.add(
                LatLng(
                    toDouble(point!![0]!!), toDouble(
                        point[1]!!
                    )
                )
            )
        }
        return points
    }

    private fun toHoles(o: Any): List<List<LatLng>> {
        val data = toList(o)
        val holes: MutableList<List<LatLng>> = ArrayList(
            data!!.size
        )
        for (rawHole in data) {
            holes.add(toPoints(rawHole))
        }
        return holes
    }

    fun interpretTileOverlayOptions(data: Map<String, *>, sink: TileOverlaySink): String {
        val fadeIn = data["fadeIn"]
        if (fadeIn != null) {
            sink.setFadeIn(toBoolean(fadeIn))
        }
        val transparency = data["transparency"]
        if (transparency != null) {
            sink.setTransparency(toFloat(transparency))
        }
        val zIndex = data["zIndex"]
        if (zIndex != null) {
            sink.setZIndex(toFloat(zIndex))
        }
        val visible = data["visible"]
        if (visible != null) {
            sink.setVisible(toBoolean(visible))
        }
        val tileOverlayId = data["tileOverlayId"] as String?
        return tileOverlayId ?: throw IllegalArgumentException("tileOverlayId was null")
    }
}
