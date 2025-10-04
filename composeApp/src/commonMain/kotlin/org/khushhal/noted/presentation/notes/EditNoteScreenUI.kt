package org.khushhal.noted.presentation.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.khushhal.noted.domain.model.Note
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreenUI(
    note: Note?, // null → add new, else → edit existing
    onBack: () -> Unit
) {
    val screenModel: NotesScreenModel = koinInject()
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    // Brown theme colors
    val backgroundColor = Color(0xFFF5EDE0) // light beige
    val primaryBrown = Color(0xFF6D4C41)    // chocolate brown
    val accentBrown = Color(0xFF8D6E63)     // lighter brown

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (note == null) "Add Note" else "Edit Note", color = primaryBrown) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("<", color = primaryBrown, style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (note == null) {
                                screenModel.createNote(
                                    Note(
                                        id = 0,
                                        title = title,
                                        content = content
                                    )
                                )
                            } else {
                                screenModel.updateNote(
                                    note.copy(
                                        title = title,
                                        content = content
                                    )
                                )
                            }
                            onBack()
                        },
                        enabled = title.isNotBlank() && content.isNotBlank()
                    ) {
                        Text("Save", color = primaryBrown, style = MaterialTheme.typography.titleMedium)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor // this works in KMP
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBrown,
                    focusedLabelColor = primaryBrown
                )
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines = Int.MAX_VALUE,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBrown,
                    focusedLabelColor = primaryBrown
                )
            )
        }
    }
}

