// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng


interface PolygonOptionsSink {
    fun setConsumeTapEvents(consumeTapEvents: Boolean)
    fun setFillColor(color: Int)
    fun setStrokeColor(color: Int)
    fun setGeodesic(geodesic: Boolean)
    fun setPoints(points: List<LatLng?>?)
    fun setHoles(holes: List<List<LatLng?>?>?)
    fun setVisible(visible: Boolean)
    fun setStrokeWidth(width: Float)
    fun setZIndex(zIndex: Int)
}
