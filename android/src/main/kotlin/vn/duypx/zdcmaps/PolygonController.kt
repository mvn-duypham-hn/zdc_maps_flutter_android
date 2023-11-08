// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.Polygon


/** Controller of a single Polygon on the map.  */
class PolygonController(
    private val polygon: Polygon,
    private var consumeTapEvents: Boolean,
    private val density: Float
) :
    PolygonOptionsSink {
    val zdcMapsPolygonId: String = polygon.id

    fun remove() {
        polygon.remove()
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
        polygon.isClickable = consumeTapEvents
    }

    override fun setFillColor(color: Int) {
        polygon.fillColor = color
    }

    override fun setStrokeColor(color: Int) {
        polygon.strokeColor = color
    }

    override fun setGeodesic(geodesic: Boolean) {
//        polygon.setGeodesic(geodesic)
    }

    override fun setPoints(points: List<LatLng?>?) {
        polygon.points = points
    }

    override fun setHoles(holes: List<List<LatLng?>?>?) {
        polygon.setHoles(holes)
    }

    override fun setVisible(visible: Boolean) {
        polygon.isVisible = visible
    }

    override fun setStrokeWidth(width: Float) {
        polygon.strokeWidth = width * density
    }

    override fun setZIndex(zIndex: Int) {
        polygon.zIndex = zIndex
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }
}
