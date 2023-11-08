// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.Circle
import com.zdc.android.zms.maps.model.LatLng

class CircleController(circle: Circle, consumeTapEvents: Boolean, density: Float) :
    CircleOptionsSink {
    private val circle: Circle
    val zdcMapsCircleId: String
    private val density: Float
    private var consumeTapEvents: Boolean

    init {
        this.circle = circle
        this.consumeTapEvents = consumeTapEvents
        this.density = density
        zdcMapsCircleId = circle.id
    }

    fun remove() {
        circle.remove()
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
        circle.isClickable = consumeTapEvents
    }

    override fun setStrokeColor(strokeColor: Int) {
        circle.strokeColor = strokeColor
    }

    override fun setFillColor(fillColor: Int) {
        circle.fillColor = fillColor
    }

    override fun setCenter(center: LatLng?) {
        circle.center = center
    }

    override fun setRadius(radius: Double) {
        circle.radius = radius
    }

    override fun setVisible(visible: Boolean) {
        circle.isVisible = visible
    }

    override fun setStrokeWidth(strokeWidth: Float) {
        circle.strokeWidth = strokeWidth * density
    }

    override fun setZIndex(zIndex: Int) {
        circle.zIndex = zIndex
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }
}
