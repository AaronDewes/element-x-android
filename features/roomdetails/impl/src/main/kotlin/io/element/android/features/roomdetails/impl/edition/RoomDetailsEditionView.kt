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

@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package io.element.android.features.roomdetails.impl.edition

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.element.android.features.createroom.api.ui.AvatarActionListView
import io.element.android.features.createroom.api.ui.LocalAvatar
import io.element.android.features.roomdetails.impl.R
import io.element.android.libraries.designsystem.components.LabelledTextField
import io.element.android.libraries.designsystem.components.avatar.Avatar
import io.element.android.libraries.designsystem.components.avatar.AvatarData
import io.element.android.libraries.designsystem.components.avatar.AvatarSize
import io.element.android.libraries.designsystem.components.button.BackButton
import io.element.android.libraries.designsystem.preview.ElementPreviewDark
import io.element.android.libraries.designsystem.preview.ElementPreviewLight
import io.element.android.libraries.designsystem.theme.LocalColors
import io.element.android.libraries.designsystem.theme.components.Icon
import io.element.android.libraries.designsystem.theme.components.Scaffold
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.designsystem.theme.components.TopAppBar
import kotlinx.coroutines.launch
import io.element.android.libraries.ui.strings.R as StringR

@Composable
fun RoomDetailsEditionView(
    state: RoomDetailsEditionState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val itemActionsBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    fun onAvatarClicked() {
        coroutineScope.launch {
            itemActionsBottomSheetState.show()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = { BackButton(onClick = onBackPressed) },
                actions = {
                    if (state.saveButtonVisible) {
                        Text(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { state.eventSink(RoomDetailsEditionEvents.Save) },
                            text = stringResource(StringR.string.action_save),
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            EditableAvatarView(state, ::onAvatarClicked)
            Spacer(modifier = Modifier.height(60.dp))
            LabelledTextField(
                label = stringResource(id = R.string.screen_create_room_room_name_label),
                value = state.roomName,
                placeholder = stringResource(id = R.string.screen_create_room_room_name_placeholder),
                singleLine = true,
                onValueChange = { state.eventSink(RoomDetailsEditionEvents.UpdateRoomName(it)) },
            )
            Spacer(modifier = Modifier.height(28.dp))
            LabelledTextField(
                label = stringResource(id = R.string.screen_create_room_topic_label),
                value = state.roomTopic,
                placeholder = stringResource(id = R.string.screen_create_room_topic_placeholder),
                maxLines = 10,
                onValueChange = { state.eventSink(RoomDetailsEditionEvents.UpdateRoomTopic(it)) },
            )
        }
    }

    AvatarActionListView(
        actions = state.avatarActions,
        modalBottomSheetState = itemActionsBottomSheetState,
        onActionSelected = { state.eventSink(RoomDetailsEditionEvents.HandleAvatarAction(it)) }
    )
}

@Composable
private fun EditableAvatarView(
    state: RoomDetailsEditionState,
    onAvatarClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clickable(onClick = onAvatarClicked)
        ) {
            if (state.localAvatarUri != null) {
                LocalAvatar(
                    avatarUri = state.localAvatarUri,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Avatar(
                    avatarData = AvatarData(state.roomId, state.roomName, state.roomAvatarUrl, size = AvatarSize.HUGE),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(LocalColors.current.gray1400)
                    .size(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.AddAPhoto,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Preview
@Composable
fun RoomDetailsEditionViewLightPreview(@PreviewParameter(RoomDetailsEditionStateProvider::class) state: RoomDetailsEditionState) =
    ElementPreviewLight { ContentToPreview(state) }

@Preview
@Composable
fun RoomDetailsEditionViewDarkPreview(@PreviewParameter(RoomDetailsEditionStateProvider::class) state: RoomDetailsEditionState) =
    ElementPreviewDark { ContentToPreview(state) }

@Composable
private fun ContentToPreview(state: RoomDetailsEditionState) {
    RoomDetailsEditionView(
        state = state,
        onBackPressed = {},
    )
}
