// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.model.LatLng

/** Receiver of Circle configuration options.  */
internal interface CircleOptionsSink {
    fun setConsumeTapEvents(consumeTapEvents: Boolean)
    fun setStrokeColor(strokeColor: Int)
    fun setFillColor(fillColor: Int)
    fun setCenter(center: LatLng?)
    fun setRadius(radius: Double)
    fun setVisible(visible: Boolean)
    fun setStrokeWidth(strokeWidth: Float)
    fun setZIndex(zIndex: Int)
}
