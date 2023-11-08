// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.graphics.Bitmap
import com.zdc.android.zms.maps.model.LatLng
import com.zdc.android.zms.maps.model.MarkerOptions


internal class MarkerBuilder : MarkerOptionsSink {
    private val markerOptions: MarkerOptions = MarkerOptions()
    private var consumeTapEvents = false

    fun build(): MarkerOptions {
        return markerOptions
    }

    fun consumeTapEvents(): Boolean {
        return consumeTapEvents
    }

    override fun setAlpha(alpha: Float) {
        markerOptions.alpha(alpha)
    }

    override fun setAnchor(u: Float, v: Float) {
        markerOptions.anchor(u, v)
    }

    override fun setConsumeTapEvents(consumeTapEvents: Boolean) {
        this.consumeTapEvents = consumeTapEvents
    }

    override fun setDraggable(draggable: Boolean) {
//        markerOptions.draggable(draggable)
    }

    override fun setFlat(flat: Boolean) {
//        markerOptions.flat(flat)
    }

    override fun setIcon(bitmapDescriptor: Bitmap?) {
        markerOptions.icon(bitmapDescriptor)
    }

    override fun setInfoWindowAnchor(u: Float, v: Float) {
        markerOptions.infoWindowAnchor(u, v)
    }

    override fun setInfoWindowText(title: String?, snippet: String?) {
        markerOptions.title(title)
        markerOptions.snippet(snippet)
    }

    override fun setPosition(position: LatLng?) {
        markerOptions.position(position)
    }

    override fun setRotation(rotation: Float) {
        markerOptions.rotation(rotation)
    }

    override fun setVisible(visible: Boolean) {
        markerOptions.visible(visible)
    }

    override fun setZIndex(zIndex: Float) {
//        markerOptions.zIndex(zIndex)
    }
}
