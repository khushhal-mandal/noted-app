package org.khushhal.noted.util

import org.khushhal.noted.data.remote.dto.NoteDto
import org.khushhal.noted.domain.model.Note

fun NoteDto.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp
    )
}

fun Note.toNoteDto(): NoteDto {
    return NoteDto(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp
    )
}