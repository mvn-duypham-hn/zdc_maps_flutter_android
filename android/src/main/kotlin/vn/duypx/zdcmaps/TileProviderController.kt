// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import android.os.Handler
import android.os.Looper
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import vn.duypx.zdcmaps.Convert.tileOverlayArgumentsToJson
import java.util.concurrent.CountDownLatch

open class TileProviderController(
    methodChannel: MethodChannel,
    protected val tileOverlayId: String
) {
}
