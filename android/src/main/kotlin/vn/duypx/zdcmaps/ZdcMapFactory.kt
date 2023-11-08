// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.content.Context
import com.zdc.android.zms.maps.model.CameraPosition
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import vn.duypx.zdcmaps.Convert.interpretZdcMapOptions
import vn.duypx.zdcmaps.Convert.toCameraPosition

class ZdcMapFactory(
    binaryMessenger: BinaryMessenger, context: Context, lifecycleProvider: LifecycleProvider
) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    private val binaryMessenger: BinaryMessenger
    private val lifecycleProvider: LifecycleProvider

    init {
        this.binaryMessenger = binaryMessenger
        this.lifecycleProvider = lifecycleProvider
    }

    override fun create(context: Context, id: Int, args: Any?): PlatformView {
        val params = args as Map<*, *>?
        val builder = ZdcMapBuilder()
        val options = params!!["options"]
        interpretZdcMapOptions(options!!, builder)
        if (params.containsKey("initialCameraPosition")) {
            val position: CameraPosition = toCameraPosition(
                params["initialCameraPosition"]!!
            )
            builder.setInitialCameraPosition(position)
        }
        if (params.containsKey("markersToAdd")) {
            builder.setInitialMarkers(params["markersToAdd"])
        }
        if (params.containsKey("polygonsToAdd")) {
            builder.setInitialPolygons(params["polygonsToAdd"])
        }
        if (params.containsKey("polylinesToAdd")) {
            builder.setInitialPolylines(params["polylinesToAdd"])
        }
        if (params.containsKey("circlesToAdd")) {
            builder.setInitialCircles(params["circlesToAdd"])
        }
        if (params.containsKey("tileOverlaysToAdd")) {
            builder.setInitialTileOverlays(params["tileOverlaysToAdd"] as List<Map<String, *>>?)
        }
        val cloudMapId = (options as Map<*, *>?)!!["cloudMapId"]
        if (cloudMapId != null) {
            builder.setMapId(cloudMapId as String?)
        }
        return builder.build(id, context, binaryMessenger, lifecycleProvider)
    }
}
