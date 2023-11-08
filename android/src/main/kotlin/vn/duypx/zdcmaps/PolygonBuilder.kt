// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.PolygonOptions


class PolygonBuilder(density: Float) : PolygonOptionsSink {
    private val polygonOptions: PolygonOptions = PolygonOptions()
    private val density: Float
    private var consumeTapEvents = false

    init {
        this.density = density
    }

    fun build(): PolygonOptions {
        return polygonOptions
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }

    override fun setFillColor(color: Int) {
        polygonOptions.fillColor(color)
    }

    override fun setStrokeColor(color: Int) {
        polygonOptions.strokeColor(color)
    }

    override fun setPoints(points: List<LatLng?>?) {
        polygonOptions.addAll(points)
    }

    override fun setHoles(holes: List<List<LatLng?>?>?) {
        for (hole in holes!!) {
            polygonOptions.addHole(hole)
        }
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
        polygonOptions.clickable(consumeTapEvents)
    }

    override fun setGeodesic(geodesic: Boolean) {
//        polygonOptions.geodesic(geodisc)
    }

    override fun setVisible(visible: Boolean) {
        polygonOptions.visible(visible)
    }

    override fun setStrokeWidth(width: Float) {
        polygonOptions.strokeWidth(width * density)
    }

    override fun setZIndex(zIndex: Int) {
        polygonOptions.zIndex(zIndex)
    }
}
