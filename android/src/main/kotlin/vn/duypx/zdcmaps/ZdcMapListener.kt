// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package vn.duypx.zdcmaps

import com.zdc.android.zms.maps.ZDCMap

interface ZdcMapListener :
    ZDCMap.OnInfoWindowClickListener,
    ZDCMap.OnMarkerClickListener,
    ZDCMap.OnPolygonClickListener,
    ZDCMap.OnPolylineClickListener,
    ZDCMap.OnCircleClickListener,
    ZDCMap.OnMapClickListener,
    ZDCMap.OnMapLongClickListener
