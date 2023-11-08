// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.ZDCMap
import com.zdc.android.zms.maps.model.Polyline
import com.zdc.android.zms.maps.model.PolylineOptions
import io.flutter.plugin.common.MethodChannel
import vn.duypx.zdcmaps.Convert.interpretPolylineOptions
import vn.duypx.zdcmaps.Convert.polylineIdToJson

class PolylinesController(methodChannel: MethodChannel, density: Float) {
    private val polylineIdToController: MutableMap<String?, PolylineController>
    private val zdcMapsPolylineIdToDartPolylineId: MutableMap<String, String>
    private val methodChannel: MethodChannel
    private var zdcMap: ZDCMap? = null
    private val density: Float

    init {
        polylineIdToController = HashMap()
        zdcMapsPolylineIdToDartPolylineId = HashMap()
        this.methodChannel = methodChannel
        this.density = density
    }

    fun setZdcMap(zdcMap: ZDCMap?) {
        this.zdcMap = zdcMap
    }

    fun addPolylines(polylinesToAdd: List<Any?>?) {
        if (polylinesToAdd != null) {
            for (polylineToAdd in polylinesToAdd) {
                addPolyline(polylineToAdd)
            }
        }
    }

    fun changePolylines(polylinesToChange: List<Any?>?) {
        if (polylinesToChange != null) {
            for (polylineToChange in polylinesToChange) {
                changePolyline(polylineToChange)
            }
        }
    }

    fun removePolylines(polylineIdsToRemove: List<Any?>?) {
        if (polylineIdsToRemove == null) {
            return
        }
        for (rawPolylineId in polylineIdsToRemove) {
            if (rawPolylineId == null) {
                continue
            }
            val polylineId = rawPolylineId as String
            val polylineController = polylineIdToController.remove(polylineId)
            if (polylineController != null) {
                polylineController.remove()
                zdcMapsPolylineIdToDartPolylineId.remove(polylineController.zdcMapsPolylineId)
            }
        }
    }

    fun onPolylineTap(zdcPolylineId: String): Boolean {
        val polylineId = zdcMapsPolylineIdToDartPolylineId[zdcPolylineId] ?: return false
        methodChannel.invokeMethod("polyline#onTap", polylineIdToJson(polylineId))
        val polylineController = polylineIdToController[polylineId]
        return polylineController?.consumeTapEvents() ?: false
    }

    private fun addPolyline(polyline: Any?) {
        if (polyline == null) {
            return
        }
        val polylineBuilder = PolylineBuilder(density)
        val polylineId = interpretPolylineOptions(polyline, polylineBuilder)
        val options: PolylineOptions = polylineBuilder.build()
        addPolyline(polylineId, options, polylineBuilder.consumeTapEvents())
    }

    private fun addPolyline(
        polylineId: String, polylineOptions: PolylineOptions, consumeTapEvents: Boolean
    ) {
        val polyline: Polyline = zdcMap!!.addPolyline(polylineOptions)
        val controller = PolylineController(polyline, consumeTapEvents, density)
        polylineIdToController[polylineId] = controller
        zdcMapsPolylineIdToDartPolylineId[polyline.getId()] = polylineId
    }

    private fun changePolyline(polyline: Any?) {
        if (polyline == null) {
            return
        }
        val polylineId = getPolylineId(polyline)
        val polylineController = polylineIdToController[polylineId]
        if (polylineController != null) {
            interpretPolylineOptions(polyline, polylineController)
        }
    }

    companion object {
        private fun getPolylineId(polyline: Any): String? {
            val polylineMap = polyline as Map<*, *>
            return polylineMap["polylineId"] as String?
        }
    }
}
