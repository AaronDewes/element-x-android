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

package io.element.android.features.leaveroom.api

import io.element.android.libraries.matrix.api.core.RoomId

data class LeaveRoomState(
    val confirmation: Confirmation = Confirmation.Hidden,
    val progress: Progress = Progress.Hidden,
    val error: Error = Error.Hidden,
    val eventSink: (LeaveRoomEvent) -> Unit = {},
) {
    sealed interface Confirmation {
        object Hidden : Confirmation
        data class Generic(val roomId: RoomId) : Confirmation
        data class PrivateRoom(val roomId: RoomId) : Confirmation
        data class LastUserInRoom(val roomId: RoomId) : Confirmation
    }

    sealed interface Progress {
        object Hidden : Progress
        object Shown : Progress
    }

    sealed interface Error {
        object Hidden : Error
        object Shown : Error
    }
}
