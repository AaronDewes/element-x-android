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

@file:OptIn(ExperimentalMaterialApi::class)

package io.element.android.features.createroom.api.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.element.android.libraries.designsystem.preview.ElementPreviewDark
import io.element.android.libraries.designsystem.preview.ElementPreviewLight
import io.element.android.libraries.designsystem.theme.components.Icon
import io.element.android.libraries.designsystem.theme.components.ModalBottomSheetLayout
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun AvatarActionListView(
    actions: ImmutableList<AvatarAction>,
    modalBottomSheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    onActionSelected: (action: AvatarAction) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    fun onItemActionClicked(itemAction: AvatarAction) {
        onActionSelected(itemAction)
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = modalBottomSheetState,
        sheetContent = {
            SheetContent(
                actions = actions,
                onActionClicked = ::onItemActionClicked,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    )
}

@Composable
private fun SheetContent(
    actions: ImmutableList<AvatarAction>,
    modifier: Modifier = Modifier,
    onActionClicked: (AvatarAction) -> Unit = { },
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = actions,
        ) { action ->
            ListItem(
                modifier = Modifier.clickable { onActionClicked(action) },
                headlineContent = {
                    Text(
                        text = stringResource(action.titleResId),
                        color = if (action.destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = stringResource(action.titleResId),
                        tint = if (action.destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun SheetContentLightPreview() =
    ElementPreviewLight { ContentToPreview() }

@Preview
@Composable
fun SheetContentDarkPreview() =
    ElementPreviewDark { ContentToPreview() }

@Composable
private fun ContentToPreview() {
    AvatarActionListView(
        actions = persistentListOf(AvatarAction.TakePhoto, AvatarAction.ChoosePhoto, AvatarAction.Remove),
        modalBottomSheetState = ModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Expanded
        ),
    )
}