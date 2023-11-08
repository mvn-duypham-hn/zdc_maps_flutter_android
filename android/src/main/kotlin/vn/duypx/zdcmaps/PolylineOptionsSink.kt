// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng

/** Receiver of Polyline configuration options.  */
interface PolylineOptionsSink {
    fun setConsumeTapEvents(consumeTapEvents: Boolean)
    fun setColor(color: Int)
    fun setGeodesic(geodesic: Boolean)
    fun setJointType(jointType: Int)
    fun setPoints(points: List<LatLng?>?)
    fun setVisible(visible: Boolean)
    fun setWidth(width: Float)
    fun setZIndex(zIndex: Int)
}
