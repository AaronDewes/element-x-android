/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.element.android.libraries.androidutils.throttler

import android.os.SystemClock

/**
 * Simple ThrottleFirst
 * See https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/throttleFirst.png
 */
class FirstThrottler(private val minimumInterval: Long = 800) {
    private var lastDate = 0L

    sealed class CanHandleResult {
        object Yes : CanHandleResult()
        data class No(val shouldWaitMillis: Long) : CanHandleResult()

        fun waitMillis(): Long {
            return when (this) {
                Yes -> 0
                is No -> shouldWaitMillis
            }
        }
    }

    fun canHandle(): CanHandleResult {
        val now = SystemClock.elapsedRealtime()
        val delaySinceLast = now - lastDate
        if (delaySinceLast > minimumInterval) {
            lastDate = now
            return CanHandleResult.Yes
        }

        // Too early
        return CanHandleResult.No(minimumInterval - delaySinceLast)
    }
}
