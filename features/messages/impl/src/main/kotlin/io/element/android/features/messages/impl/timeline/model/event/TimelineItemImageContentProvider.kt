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

package io.element.android.features.messages.impl.timeline.model.event

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.media3.common.MimeTypes
import io.element.android.libraries.matrix.api.media.MediaSource

open class TimelineItemImageContentProvider : PreviewParameterProvider<TimelineItemImageContent> {
    override val values: Sequence<TimelineItemImageContent>
        get() = sequenceOf(
            aTimelineItemImageContent(),
            aTimelineItemImageContent().copy(aspectRatio = 1.0f),
            aTimelineItemImageContent().copy(aspectRatio = 1.5f),
        )
}

fun aTimelineItemImageContent() = TimelineItemImageContent(
    body = "a body",
    mediaSource = MediaSource(""),
    mimeType = MimeTypes.IMAGE_JPEG,
    blurhash = "TQF5:I_NtRE4kXt7Z#MwkCIARPjr",
    width = null,
    height = 300,
    aspectRatio = 0.5f
)
