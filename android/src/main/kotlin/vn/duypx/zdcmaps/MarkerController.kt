// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.graphics.Bitmap
import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.Marker


/** Controller of a single Marker on the map.  */
internal class MarkerController(marker: Marker, consumeTapEvents: Boolean) :
    MarkerOptionsSink {
    private val marker: Marker
    val zdcMapsMarkerId: String
    private var consumeTapEvents: Boolean

    init {
        this.marker = marker
        this.consumeTapEvents = consumeTapEvents
        zdcMapsMarkerId = marker.id
    }

    fun remove() {
        marker.remove()
    }

    override fun setAlpha(alpha: Float) {
        marker.alpha = alpha
    }

    override fun setAnchor(u: Float, v: Float) {
        marker.setAnchor(u, v)
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
    }

    override fun setDraggable(draggable: Boolean) {
//        marker.setDraggable(draggable)
    }

    override fun setFlat(flat: Boolean) {
//        marker.setFlat(flat)
    }

    override fun setIcon(bitmapDescriptor: Bitmap?) {
        marker.setIcon(bitmapDescriptor)
    }

    override fun setInfoWindowAnchor(u: Float, v: Float) {
        marker.setInfoWindowAnchor(u, v)
    }

    override fun setInfoWindowText(title: String?, snippet: String?) {
        marker.title = title
        marker.snippet = snippet
    }

    override fun setPosition(position: LatLng?) {
        marker.position = position
    }

    override fun setRotation(rotation: Float) {
        marker.rotation = rotation
    }

    override fun setVisible(visible: Boolean) {
        marker.isVisible = visible
    }

    override fun setZIndex(zIndex: Float) {
//        marker.setZIndex(zIndex)
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }

    fun showInfoWindow() {
        marker.showInfoWindow()
    }

    fun hideInfoWindow() {
        marker.hideInfoWindow()
    }

    val isInfoWindowShown: Boolean
        get() = marker.isInfoWindowShown
}
