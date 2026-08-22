@file:Suppress("ClassName", "EnumEntryName", "ConstPropertyName")

/*
 * Copyright (c)  2026 Zakir Sheikh
 *
 * Created by sheik on 4 of Jan 2026
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last Modified by sheik on 4 of Jan 2026
 *
 */

package com.zs.audiofy.common

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.RectangleShape
import com.zs.audiofy.common.shapes.CompactDisk
import com.zs.audiofy.common.shapes.SunnyShape

/**
 * Common access point for app-level constants and resources.
 *
 * Mirrors the naming convention of Android's [R] class but provides a more
 * flexible, centralized extension. Inspired by Kotlin Multiplatform patterns,
 * this reduces direct dependency on the generated [R] class, which is often
 * cumbersome to access during typing.
 * @see string
 * @see drawable
 * @see manifest
 * @see shape
 * @see action
 * @see dimen
 */
object Res {

    // Typealiases for direct access to Android resources (R.string, R.drawable, etc.)
    typealias string = com.zs.audiofy.R.string
    typealias drawable = com.zs.audiofy.R.drawable
    typealias raw = com.zs.audiofy.R.raw
    typealias array = com.zs.audiofy.R.array
    typealias plurals = com.zs.audiofy.R.plurals

    /**
     * Common access to Compose shapes.
     */
    object shape {
        val circle = CircleShape
        val rectangle = RectangleShape
        val sunny = SunnyShape(0.4f)
        val compact_disk get() = CompactDisk
    }

    /**
     * App-related constants and intents.
     *
     * Provides URIs, package names, default colors, and permission lists
     * required for app configuration and external navigation.
     */
    object app {
        /**
         * Utility to check if the current SDK version is at least [api].
         */
        @ChecksSdkIntAtLeast(parameter = 0)
        fun isAtLeast(api: Int) = Build.VERSION.SDK_INT >= api
    }
}