// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.content.Context
import com.zdc.android.zms.maps.ZDCMap
import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.Marker
import com.zdc.android.zms.maps.model.MarkerOptions
import io.flutter.plugin.common.MethodChannel
import vn.duypx.zdcmaps.Convert.interpretMarkerOptions
import vn.duypx.zdcmaps.Convert.latLngToJson
import vn.duypx.zdcmaps.Convert.markerIdToJson

internal class MarkersController(methodChannel: MethodChannel, context: Context) {
    private val markerIdToController: MutableMap<String?, MarkerController>
    private val zdcMapsMarkerIdToDartMarkerId: MutableMap<String, String>
    private val methodChannel: MethodChannel
    private var zdcMap: ZDCMap? = null
    private var context: Context

    init {
        markerIdToController = HashMap()
        zdcMapsMarkerIdToDartMarkerId = HashMap()
        this.methodChannel = methodChannel
        this.context = context
    }

    fun setZdcMap(zdcMap: ZDCMap?) {
        this.zdcMap = zdcMap
    }

    fun addMarkers(markersToAdd: List<Any?>?) {
        if (markersToAdd != null) {
            for (markerToAdd in markersToAdd) {
                addMarker(markerToAdd)
            }
        }
    }

    fun changeMarkers(markersToChange: List<Any?>?) {
        if (markersToChange != null) {
            for (markerToChange in markersToChange) {
                changeMarker(markerToChange)
            }
        }
    }

    fun removeMarkers(markerIdsToRemove: List<Any?>?) {
        if (markerIdsToRemove == null) {
            return
        }
        for (rawMarkerId in markerIdsToRemove) {
            if (rawMarkerId == null) {
                continue
            }
            val markerId = rawMarkerId as String
            val markerController = markerIdToController.remove(markerId)
            if (markerController != null) {
                markerController.remove()
                zdcMapsMarkerIdToDartMarkerId.remove(markerController.zdcMapsMarkerId)
            }
        }
    }

    fun showMarkerInfoWindow(markerId: String?, result: MethodChannel.Result) {
        val markerController = markerIdToController[markerId]
        if (markerController != null) {
            markerController.showInfoWindow()
            result.success(null)
        } else {
            result.error("Invalid markerId", "showInfoWindow called with invalid markerId", null)
        }
    }

    fun hideMarkerInfoWindow(markerId: String?, result: MethodChannel.Result) {
        val markerController = markerIdToController[markerId]
        if (markerController != null) {
            markerController.hideInfoWindow()
            result.success(null)
        } else {
            result.error("Invalid markerId", "hideInfoWindow called with invalid markerId", null)
        }
    }

    fun isInfoWindowShown(markerId: String?, result: MethodChannel.Result) {
        val markerController = markerIdToController[markerId]
        if (markerController != null) {
            result.success(markerController.isInfoWindowShown)
        } else {
            result.error("Invalid markerId", "isInfoWindowShown called with invalid markerId", null)
        }
    }

    fun onMarkerTap(zdcMarkerId: String): Boolean {
        val markerId = zdcMapsMarkerIdToDartMarkerId[zdcMarkerId] ?: return false
        methodChannel.invokeMethod("marker#onTap", markerIdToJson(markerId))
        val markerController = markerIdToController[markerId]
        return markerController?.consumeTapEvents() ?: false
    }

    fun onMarkerDragStart(zdcMarkerId: String, latLng: LatLng?) {
        val markerId = zdcMapsMarkerIdToDartMarkerId[zdcMarkerId] ?: return
        val data: MutableMap<String, Any> = HashMap()
        data["markerId"] = markerId
        data["position"] = latLngToJson(latLng!!)
        methodChannel.invokeMethod("marker#onDragStart", data)
    }

    fun onMarkerDrag(zdcMarkerId: String, latLng: LatLng?) {
        val markerId = zdcMapsMarkerIdToDartMarkerId[zdcMarkerId] ?: return
        val data: MutableMap<String, Any> = HashMap()
        data["markerId"] = markerId
        data["position"] = latLngToJson(latLng!!)
        methodChannel.invokeMethod("marker#onDrag", data)
    }

    fun onMarkerDragEnd(zdcMarkerId: String, latLng: LatLng?) {
        val markerId = zdcMapsMarkerIdToDartMarkerId[zdcMarkerId] ?: return
        val data: MutableMap<String, Any> = HashMap()
        data["markerId"] = markerId
        data["position"] = latLngToJson(latLng!!)
        methodChannel.invokeMethod("marker#onDragEnd", data)
    }

    fun onInfoWindowTap(zdcMarkerId: String) {
        val markerId = zdcMapsMarkerIdToDartMarkerId[zdcMarkerId] ?: return
        methodChannel.invokeMethod("infoWindow#onTap", markerIdToJson(markerId))
    }

    private fun addMarker(marker: Any?) {
        if (marker == null) {
            return
        }
        val markerBuilder = MarkerBuilder()
        val markerId = interpretMarkerOptions(marker, markerBuilder, context)
        val options: MarkerOptions = markerBuilder.build()
        addMarker(markerId, options, markerBuilder.consumeTapEvents())
    }

    private fun addMarker(
        markerId: String,
        markerOptions: MarkerOptions,
        consumeTapEvents: Boolean
    ) {
        val marker: Marker = zdcMap!!.addMarker(markerOptions)
        val controller = MarkerController(marker, consumeTapEvents)
        markerIdToController[markerId] = controller
        zdcMapsMarkerIdToDartMarkerId[marker.id] = markerId
    }

    private fun changeMarker(marker: Any?) {
        if (marker == null) {
            return
        }
        val markerId = getMarkerId(marker)
        val markerController = markerIdToController[markerId]
        if (markerController != null) {
            interpretMarkerOptions(marker, markerController, context)
        }
    }

    companion object {
        private fun getMarkerId(marker: Any): String? {
            val markerMap = marker as Map<*, *>
            return markerMap["markerId"] as String?
        }
    }
}
