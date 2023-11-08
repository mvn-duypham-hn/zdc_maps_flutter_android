// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps


class TileOverlayController() : TileOverlaySink {

    init {
    }

    fun remove() {
    }

    fun clearTileCache() {
    }

    val tileOverlayInfo: Map<String, Any>
        get() {
            val tileOverlayInfo: MutableMap<String, Any> = HashMap()
            return tileOverlayInfo
        }

    override fun setFadeIn(fadeIn: Boolean) {
//        tileOverlay.setFadeIn(fadeIn)
    }

    override fun setTransparency(transparency: Float) {
//        tileOverlay.setTransparency(transparency)
    }

    override fun setZIndex(zIndex: Float) {
//        tileOverlay.setZIndex(zIndex)
    }

    override fun setVisible(visible: Boolean) {
//        tileOverlay.setVisible(visible)
    }

}
