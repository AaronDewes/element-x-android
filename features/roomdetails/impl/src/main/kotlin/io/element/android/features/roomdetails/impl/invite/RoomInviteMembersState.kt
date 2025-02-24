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

package io.element.android.features.roomdetails.impl.invite

import io.element.android.libraries.designsystem.theme.components.SearchBarResultState
import io.element.android.libraries.matrix.api.user.MatrixUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RoomInviteMembersState(
    val canInvite: Boolean = false,
    val searchQuery: String = "",
    val searchResults: SearchBarResultState<ImmutableList<InvitableUser>> = SearchBarResultState.NotSearching(),
    val selectedUsers: ImmutableList<MatrixUser> = persistentListOf(),
    val isSearchActive: Boolean = false,
    val eventSink: (RoomInviteMembersEvents) -> Unit = {},
)

data class InvitableUser(
    val matrixUser: MatrixUser,
    val isSelected: Boolean = false,
    val isAlreadyJoined: Boolean = false,
    val isAlreadyInvited: Boolean = false,
)
