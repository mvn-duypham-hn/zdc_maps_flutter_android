// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.CircleOptions
import com.zdc.android.zms.maps.model.LatLng

internal class CircleBuilder(density: Float) : CircleOptionsSink {
    private val circleOptions: CircleOptions = CircleOptions()
    private val density: Float
    private var consumeTapEvents = false

    init {
        this.density = density
    }

    fun build(): CircleOptions {
        return circleOptions
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }

    override fun setFillColor(fillColor: Int) {
        circleOptions.fillColor(fillColor)
    }

    override fun setStrokeColor(strokeColor: Int) {
        circleOptions.strokeColor(strokeColor)
    }

    override fun setCenter(center: LatLng?) {
        circleOptions.center(center)
    }

    override fun setRadius(radius: Double) {
        circleOptions.radius(radius)
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
        circleOptions.clickable(consumeTapEvents)
    }

    override fun setVisible(visible: Boolean) {
        circleOptions.visible(visible)
    }

    override fun setStrokeWidth(strokeWidth: Float) {
        circleOptions.strokeWidth(strokeWidth * density)
    }

    override fun setZIndex(zIndex: Int) {
        circleOptions.zIndex(zIndex)
    }
}
