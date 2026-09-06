package com.zs.audiofy.common.impl

import coil3.Extras
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.CachePolicy
import coil3.request.Options
import coil3.video.VideoFrameDecoder
import coil3.video.videoFrameIndex
import coil3.video.videoFrameMicros
import coil3.video.videoFramePercent

/*
 * Copyright 2025 sheik
 *
 * Created by sheik on 09-05-2025.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class VideoFrameDecoderFactory : Decoder.Factory {

    // Cache extras if needed (can be reused later for performance)
    var cachedDefaultExtras: Extras? = null
    val DEFAULT_FRAME_AT_PCT = 0.02
    val KEY_VIDEO_FRAME_AT = Extras.Key.videoFramePercent

    override fun create(
        result: SourceFetchResult,
        options: Options,
        imageLoader: coil3.ImageLoader
    ): Decoder? {
        // Check the MIME type of the source.
        // If it's null or not a video (doesn't start with "video/"), return null
        // so Coil falls back to other decoders (like BitmapFactoryDecoder for images).
        val mimeType = result.mimeType
        if (mimeType == null || !mimeType.startsWith("video/")) {
            return null
        }


        // ✅ Step 2: Determine if caller explicitly defined a frame position
        // Options allow specifying frame percent, index, or timestamp.
        val isPositionDefined = options.videoFramePercent != -1.0 ||
                options.videoFrameIndex != -1 ||
                options.videoFrameMicros != -1L

        // ✅ Step 3: Merge caller options with defaults
        // If no position is defined, we set a sensible default (2% into the video).
        val newOptions = when {
            // Caller already defined a frame → keep options as-is
            isPositionDefined -> options
            cachedDefaultExtras != null -> options.copy(extras = cachedDefaultExtras!!, diskCachePolicy = CachePolicy.ENABLED)
            else -> {
                val extras = options.extras.newBuilder().set(KEY_VIDEO_FRAME_AT, DEFAULT_FRAME_AT_PCT).build()
                if (options.extras == Extras.EMPTY)
                    cachedDefaultExtras = extras
                options.copy(extras =  extras, diskCachePolicy = CachePolicy.ENABLED)
            }
        }

        // ✅ Step 4: Return a VideoFrameDecoder
        // VideoFrameDecoder uses MediaMetadataRetriever internally to extract a frame.
        // By default, it grabs the first frame (time = -1), but here we override
        // with our merged options (e.g., frame percent, index, micros).
        return VideoFrameDecoder(result.source, newOptions)
    }
}