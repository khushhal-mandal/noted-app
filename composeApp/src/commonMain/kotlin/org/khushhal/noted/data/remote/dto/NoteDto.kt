package org.khushhal.noted.data.remote.dto

import kotlinx.serialization.Serializable
import org.khushhal.noted.domain.model.Note

@Serializable
data class NoteDto (
    val id: Int? = null,
    val title: String? = null,
    val content: String? = null,
    val timestamp: String? = null
)