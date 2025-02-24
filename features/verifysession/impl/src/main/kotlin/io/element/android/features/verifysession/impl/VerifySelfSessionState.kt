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

package io.element.android.features.verifysession.impl

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import io.element.android.libraries.architecture.Async
import io.element.android.libraries.matrix.api.verification.VerificationEmoji

@Immutable
data class VerifySelfSessionState(
    val verificationFlowStep: VerificationStep,
    val eventSink: (VerifySelfSessionViewEvents) -> Unit,
) {

    @Stable
    sealed interface VerificationStep {
        object Initial : VerificationStep
        object Canceled : VerificationStep
        object AwaitingOtherDeviceResponse : VerificationStep
        object Ready : VerificationStep
        data class Verifying(val emojiList: List<VerificationEmoji>, val state: Async<Unit>) : VerificationStep
        object Completed : VerificationStep
    }
}
