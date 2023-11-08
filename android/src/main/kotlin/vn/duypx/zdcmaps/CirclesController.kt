// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.ZDCMap
import com.zdc.android.zms.maps.model.Circle
import com.zdc.android.zms.maps.model.CircleOptions
import io.flutter.plugin.common.MethodChannel

class CirclesController(methodChannel: MethodChannel, density: Float) {
    private val circleIdToController: MutableMap<String?, CircleController>
    private val zdcMapsCircleIdToDartCircleId: MutableMap<String, String>
    private val methodChannel: MethodChannel
    private val density: Float
    private var zdcMap: ZDCMap? = null

    init {
        circleIdToController = HashMap()
        zdcMapsCircleIdToDartCircleId = HashMap()
        this.methodChannel = methodChannel
        this.density = density
    }

    fun setZdcMap(zdcMap: ZDCMap?) {
        this.zdcMap = zdcMap
    }

    fun addCircles(circlesToAdd: List<Any?>?) {
        if (circlesToAdd != null) {
            for (circleToAdd in circlesToAdd) {
                addCircle(circleToAdd)
            }
        }
    }

    fun changeCircles(circlesToChange: List<Any?>?) {
        if (circlesToChange != null) {
            for (circleToChange in circlesToChange) {
                changeCircle(circleToChange)
            }
        }
    }

    fun removeCircles(circleIdsToRemove: List<Any?>?) {
        if (circleIdsToRemove == null) {
            return
        }
        for (rawCircleId in circleIdsToRemove) {
            if (rawCircleId == null) {
                continue
            }
            val circleId = rawCircleId as String
            val circleController = circleIdToController.remove(circleId)
            if (circleController != null) {
                circleController.remove()
                zdcMapsCircleIdToDartCircleId.remove(circleController.zdcMapsCircleId)
            }
        }
    }

    fun onCircleTap(zdcCircleId: String): Boolean {
        val circleId = zdcMapsCircleIdToDartCircleId[zdcCircleId] ?: return false
        methodChannel.invokeMethod("circle#onTap", Convert.circleIdToJson(circleId))
        val circleController = circleIdToController[circleId]
        return circleController?.consumeTapEvents() ?: false
    }

    private fun addCircle(circle: Any?) {
        if (circle == null) {
            return
        }
        val circleBuilder = CircleBuilder(density)
        val circleId: String = Convert.interpretCircleOptions(circle, circleBuilder)
        val options: CircleOptions = circleBuilder.build()
        addCircle(circleId, options, circleBuilder.consumeTapEvents())
    }

    private fun addCircle(
        circleId: String,
        circleOptions: CircleOptions,
        consumeTapEvents: Boolean
    ) {
        val circle: Circle = zdcMap!!.addCircle(circleOptions)
        val controller = CircleController(circle, consumeTapEvents, density)
        circleIdToController[circleId] = controller
        zdcMapsCircleIdToDartCircleId[circle.id] = circleId
    }

    private fun changeCircle(circle: Any?) {
        if (circle == null) {
            return
        }
        val circleId = getCircleId(circle)
        val circleController = circleIdToController[circleId]
        if (circleController != null) {
            Convert.interpretCircleOptions(circle, circleController)
        }
    }

    companion object {
        private fun getCircleId(circle: Any): String? {
            val circleMap = circle as Map<*, *>
            return circleMap["circleId"] as String?
        }
    }
}
