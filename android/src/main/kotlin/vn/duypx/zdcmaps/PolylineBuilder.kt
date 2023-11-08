// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.PolylineOptions


internal class PolylineBuilder(density: Float) : PolylineOptionsSink {
    private val polylineOptions: PolylineOptions = PolylineOptions()
    private var consumeTapEvents = false
    private val density: Float

    init {
        this.density = density
    }

    fun build(): PolylineOptions {
        return polylineOptions
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }

    override fun setColor(color: Int) {
        polylineOptions.color(color)
    }

    override fun setJointType(jointType: Int) {
//        polylineOptions.jointType(jointType)
    }

    override fun setPoints(points: List<LatLng?>?) {
        polylineOptions.addAll(points)
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
        polylineOptions.clickable(consumeTapEvents)
    }

    override fun setGeodesic(geodesic: Boolean) {
//        polylineOptions.geodesic(geodesic)
    }

    override fun setVisible(visible: Boolean) {
        polylineOptions.visible(visible)
    }

    override fun setWidth(width: Float) {
        polylineOptions.width(width * density)
    }

    override fun setZIndex(zIndex: Int) {
        polylineOptions.zIndex(zIndex)
    }
}
