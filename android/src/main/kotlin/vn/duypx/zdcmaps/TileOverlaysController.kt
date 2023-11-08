// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.ZDCMap
import io.flutter.plugin.common.MethodChannel
import vn.duypx.zdcmaps.Convert.interpretTileOverlayOptions

internal class TileOverlaysController(methodChannel: MethodChannel) {
    private val tileOverlayIdToController: MutableMap<String?, TileOverlayController>
    private val methodChannel: MethodChannel
    private var zdcMap: ZDCMap? = null

    init {
        tileOverlayIdToController = HashMap()
        this.methodChannel = methodChannel
    }

    fun setZdcMap(zdcMap: ZDCMap?) {
        this.zdcMap = zdcMap
    }

    fun addTileOverlays(tileOverlaysToAdd: List<Map<String, *>>?) {
        if (tileOverlaysToAdd == null) {
            return
        }
        for (tileOverlayToAdd in tileOverlaysToAdd) {
            addTileOverlay(tileOverlayToAdd)
        }
    }

    fun changeTileOverlays(
        tileOverlaysToChange:
        List<Map<String, *>>
    ) {
        for (tileOverlayToChange in tileOverlaysToChange) {
            changeTileOverlay(tileOverlayToChange)
        }
    }

    fun removeTileOverlays(tileOverlayIdsToRemove: List<String?>?) {
        if (tileOverlayIdsToRemove == null) {
            return
        }
        for (tileOverlayId in tileOverlayIdsToRemove) {
            if (tileOverlayId == null) {
                continue
            }
            removeTileOverlay(tileOverlayId)
        }
    }

    fun clearTileCache(tileOverlayId: String?) {
        if (tileOverlayId == null) {
            return
        }
        val tileOverlayController = tileOverlayIdToController[tileOverlayId]
        tileOverlayController?.clearTileCache()
    }

    fun getTileOverlayInfo(tileOverlayId: String?): Map<String, Any>? {
        if (tileOverlayId == null) {
            return null
        }
        val tileOverlayController = tileOverlayIdToController[tileOverlayId] ?: return null
        return tileOverlayController.tileOverlayInfo
    }

    private fun addTileOverlay(tileOverlayOptions: Map<String, *>) {
    }

    private fun changeTileOverlay(tileOverlayOptions: Map<String, *>) {
        val tileOverlayId = getTileOverlayId(tileOverlayOptions)
        val tileOverlayController = tileOverlayIdToController[tileOverlayId]
        if (tileOverlayController != null) {
            interpretTileOverlayOptions(tileOverlayOptions, tileOverlayController)
        }
    }

    private fun removeTileOverlay(tileOverlayId: String) {
        val tileOverlayController = tileOverlayIdToController[tileOverlayId]
        if (tileOverlayController != null) {
            tileOverlayController.remove()
            tileOverlayIdToController.remove(tileOverlayId)
        }
    }

    companion object {
        private fun getTileOverlayId(tileOverlay: Map<String, *>): String? {
            return tileOverlay["tileOverlayId"] as String?
        }
    }
}
