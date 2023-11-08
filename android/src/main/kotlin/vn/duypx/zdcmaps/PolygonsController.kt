// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.ZDCMap
import com.zdc.android.zms.maps.model.Polygon
import com.zdc.android.zms.maps.model.PolygonOptions
import io.flutter.plugin.common.MethodChannel
import vn.duypx.zdcmaps.Convert.interpretPolygonOptions
import vn.duypx.zdcmaps.Convert.polygonIdToJson


class PolygonsController(methodChannel: MethodChannel, density: Float) {
    private val polygonIdToController: MutableMap<String?, PolygonController>
    private val zdcMapsPolygonIdToDartPolygonId: MutableMap<String, String>
    private val methodChannel: MethodChannel
    private val density: Float
    private var zdceMap: ZDCMap? = null

    init {
        polygonIdToController = HashMap()
        zdcMapsPolygonIdToDartPolygonId = HashMap()
        this.methodChannel = methodChannel
        this.density = density
    }

    fun setZdcMap(zdcMap: ZDCMap?) {
        this.zdceMap = zdcMap
    }

    fun addPolygons(polygonsToAdd: List<Any?>?) {
        if (polygonsToAdd != null) {
            for (polygonToAdd in polygonsToAdd) {
                addPolygon(polygonToAdd)
            }
        }
    }

    fun changePolygons(polygonsToChange: List<Any?>?) {
        if (polygonsToChange != null) {
            for (polygonToChange in polygonsToChange) {
                changePolygon(polygonToChange)
            }
        }
    }

    fun removePolygons(polygonIdsToRemove: List<Any?>?) {
        if (polygonIdsToRemove == null) {
            return
        }
        for (rawPolygonId in polygonIdsToRemove) {
            if (rawPolygonId == null) {
                continue
            }
            val polygonId = rawPolygonId as String
            val polygonController = polygonIdToController.remove(polygonId)
            if (polygonController != null) {
                polygonController.remove()
                zdcMapsPolygonIdToDartPolygonId.remove(polygonController.zdcMapsPolygonId)
            }
        }
    }

    fun onPolygonTap(zdcPolygonId: String): Boolean {
        val polygonId = zdcMapsPolygonIdToDartPolygonId[zdcPolygonId] ?: return false
        methodChannel.invokeMethod("polygon#onTap", polygonIdToJson(polygonId))
        val polygonController = polygonIdToController[polygonId]
        return polygonController?.consumeTapEvents() ?: false
    }

    private fun addPolygon(polygon: Any?) {
        if (polygon == null) {
            return
        }
        val polygonBuilder = PolygonBuilder(density)
        val polygonId = interpretPolygonOptions(polygon, polygonBuilder)
        val options: PolygonOptions = polygonBuilder.build()
        addPolygon(polygonId, options, polygonBuilder.consumeTapEvents())
    }

    private fun addPolygon(
        polygonId: String, polygonOptions: PolygonOptions, consumeTapEvents: Boolean
    ) {
        val polygon: Polygon = zdceMap!!.addPolygon(polygonOptions)
        val controller = PolygonController(polygon, consumeTapEvents, density)
        polygonIdToController[polygonId] = controller
        zdcMapsPolygonIdToDartPolygonId[polygon.id] = polygonId
    }

    private fun changePolygon(polygon: Any?) {
        if (polygon == null) {
            return
        }
        val polygonId = getPolygonId(polygon)
        val polygonController = polygonIdToController[polygonId]
        if (polygonController != null) {
            interpretPolygonOptions(polygon, polygonController)
        }
    }

    companion object {
        private fun getPolygonId(polygon: Any): String? {
            val polygonMap = polygon as Map<*, *>
            return polygonMap["polygonId"] as String?
        }
    }
}
