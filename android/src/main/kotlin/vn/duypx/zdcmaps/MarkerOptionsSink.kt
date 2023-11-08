// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.graphics.Bitmap
import com.zdc.android.zms.maps.model.LatLng

/** Receiver of Marker configuration options.  */
internal interface MarkerOptionsSink {
    fun setAlpha(alpha: Float)
    fun setAnchor(u: Float, v: Float)
    fun setConsumeTapEvents(consumeTapEvents: Boolean)
    fun setDraggable(draggable: Boolean)
    fun setFlat(flat: Boolean)
//    fun setIcon(bitmapDescriptor: BitmapDescriptor?)
    fun setIcon(bitmapDescriptor: Bitmap?)
    fun setInfoWindowAnchor(u: Float, v: Float)
    fun setInfoWindowText(title: String?, snippet: String?)
    fun setPosition(position: LatLng?)
    fun setRotation(rotation: Float)
    fun setVisible(visible: Boolean)
    fun setZIndex(zIndex: Float)
}
