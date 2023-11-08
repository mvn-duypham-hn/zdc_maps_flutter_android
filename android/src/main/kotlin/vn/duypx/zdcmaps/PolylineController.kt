// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.Polyline


/** Controller of a single Polyline on the map.  */
class PolylineController(polyline: Polyline, consumeTapEvents: Boolean, density: Float) :
    PolylineOptionsSink {
    private val polyline: Polyline
    val zdcMapsPolylineId: String
    private var consumeTapEvents: Boolean
    private val density: Float

    init {
        this.polyline = polyline
        this.consumeTapEvents = consumeTapEvents
        this.density = density
        zdcMapsPolylineId = polyline.id
    }

    fun remove() {
        polyline.remove()
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
        polyline.isClickable = consumeTapEvents
    }

    override fun setColor(color: Int) {
        polyline.color = color
    }

    override fun setGeodesic(geodesic: Boolean) {
//        polyline.setGeodesic(geodesic)
    }

    override fun setJointType(jointType: Int) {
//        polyline.setJointType(jointType)
    }

    override fun setPoints(points: List<LatLng?>?) {
        polyline.points = points
    }

    override fun setVisible(visible: Boolean) {
        polyline.isVisible = visible
    }

    override fun setWidth(width: Float) {
        polyline.width = width * density
    }

    override fun setZIndex(zIndex: Int) {
        polyline.zIndex = zIndex
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }
}
