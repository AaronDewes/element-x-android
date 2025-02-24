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

package io.element.android.services.appnavstate.impl

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.core.log.logger.LoggerTag
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.di.SingleIn
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.matrix.api.core.SessionId
import io.element.android.libraries.matrix.api.core.SpaceId
import io.element.android.libraries.matrix.api.core.ThreadId
import io.element.android.services.appnavstate.api.AppNavigationState
import io.element.android.services.appnavstate.api.AppNavigationStateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

private val loggerTag = LoggerTag("Navigation")

/**
 * TODO This will maybe not support properly navigation using permalink.
 */
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class DefaultAppNavigationStateService @Inject constructor() : AppNavigationStateService {

    private val currentAppNavigationState: MutableStateFlow<AppNavigationState> = MutableStateFlow(AppNavigationState.Root)
    override val appNavigationStateFlow: StateFlow<AppNavigationState> = currentAppNavigationState

    override fun onNavigateToSession(owner: String, sessionId: SessionId) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Navigating to session $sessionId. Current state: $currentValue")
        val newValue: AppNavigationState.Session = when (currentValue) {
            is AppNavigationState.Session,
            is AppNavigationState.Space,
            is AppNavigationState.Room,
            is AppNavigationState.Thread,
            is AppNavigationState.Root -> AppNavigationState.Session(owner, sessionId)
        }
        currentAppNavigationState.value = newValue
    }

    override fun onNavigateToSpace(owner: String, spaceId: SpaceId) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Navigating to space $spaceId. Current state: $currentValue")
        val newValue: AppNavigationState.Space = when (currentValue) {
            AppNavigationState.Root -> error("onNavigateToSession() must be called first")
            is AppNavigationState.Session -> AppNavigationState.Space(owner, spaceId, currentValue)
            is AppNavigationState.Space -> AppNavigationState.Space(owner, spaceId, currentValue.parentSession)
            is AppNavigationState.Room -> AppNavigationState.Space(owner, spaceId, currentValue.parentSpace.parentSession)
            is AppNavigationState.Thread -> AppNavigationState.Space(owner, spaceId, currentValue.parentRoom.parentSpace.parentSession)
        }
        currentAppNavigationState.value = newValue
    }

    override fun onNavigateToRoom(owner: String, roomId: RoomId) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Navigating to room $roomId. Current state: $currentValue")
        val newValue: AppNavigationState.Room = when (currentValue) {
            AppNavigationState.Root -> error("onNavigateToSession() must be called first")
            is AppNavigationState.Session -> error("onNavigateToSpace() must be called first")
            is AppNavigationState.Space -> AppNavigationState.Room(owner, roomId, currentValue)
            is AppNavigationState.Room -> AppNavigationState.Room(owner, roomId, currentValue.parentSpace)
            is AppNavigationState.Thread -> AppNavigationState.Room(owner, roomId, currentValue.parentRoom.parentSpace)
        }
        currentAppNavigationState.value = newValue
    }

    override fun onNavigateToThread(owner: String, threadId: ThreadId) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Navigating to thread $threadId. Current state: $currentValue")
        val newValue: AppNavigationState.Thread = when (currentValue) {
            AppNavigationState.Root -> error("onNavigateToSession() must be called first")
            is AppNavigationState.Session -> error("onNavigateToSpace() must be called first")
            is AppNavigationState.Space -> error("onNavigateToRoom() must be called first")
            is AppNavigationState.Room -> AppNavigationState.Thread(owner, threadId, currentValue)
            is AppNavigationState.Thread -> AppNavigationState.Thread(owner, threadId, currentValue.parentRoom)
        }
        currentAppNavigationState.value = newValue
    }

    override fun onLeavingThread(owner: String) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Leaving thread. Current state: $currentValue")
        if (!currentValue.assertOwner(owner)) return
        val newValue: AppNavigationState.Room = when (currentValue) {
            AppNavigationState.Root -> error("onNavigateToSession() must be called first")
            is AppNavigationState.Session -> error("onNavigateToSpace() must be called first")
            is AppNavigationState.Space -> error("onNavigateToRoom() must be called first")
            is AppNavigationState.Room -> error("onNavigateToThread() must be called first")
            is AppNavigationState.Thread -> currentValue.parentRoom
        }
        currentAppNavigationState.value = newValue
    }

    override fun onLeavingRoom(owner: String) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Leaving room. Current state: $currentValue")
        if (!currentValue.assertOwner(owner)) return
        val newValue: AppNavigationState.Space = when (currentValue) {
            AppNavigationState.Root -> error("onNavigateToSession() must be called first")
            is AppNavigationState.Session -> error("onNavigateToSpace() must be called first")
            is AppNavigationState.Space -> error("onNavigateToRoom() must be called first")
            is AppNavigationState.Room -> currentValue.parentSpace
            is AppNavigationState.Thread -> currentValue.parentRoom.parentSpace
        }
        currentAppNavigationState.value = newValue
    }

    override fun onLeavingSpace(owner: String) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Leaving space. Current state: $currentValue")
        if (!currentValue.assertOwner(owner)) return
        val newValue: AppNavigationState.Session = when (currentValue) {
            AppNavigationState.Root -> error("onNavigateToSession() must be called first")
            is AppNavigationState.Session -> error("onNavigateToSpace() must be called first")
            is AppNavigationState.Space -> currentValue.parentSession
            is AppNavigationState.Room -> currentValue.parentSpace.parentSession
            is AppNavigationState.Thread -> currentValue.parentRoom.parentSpace.parentSession
        }
        currentAppNavigationState.value = newValue
    }

    override fun onLeavingSession(owner: String) {
        val currentValue = currentAppNavigationState.value
        Timber.tag(loggerTag.value).d("Leaving session. Current state: $currentValue")
        if (!currentValue.assertOwner(owner)) return
        currentAppNavigationState.value = AppNavigationState.Root
    }

    private fun AppNavigationState.assertOwner(owner: String): Boolean {
        if (this.owner != owner) {
            Timber.tag(loggerTag.value).d("Can't leave current state as the owner is not the same (current = ${this.owner}, new = $owner)")
            return false
        }
        return true
    }
}
