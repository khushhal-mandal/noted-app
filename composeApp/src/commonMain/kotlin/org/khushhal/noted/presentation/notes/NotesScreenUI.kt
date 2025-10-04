package org.khushhal.noted.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.khushhal.noted.domain.model.Note
import org.khushhal.noted.util.ResultState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreenUI(
    onNoteClick: (Note) -> Unit,
    onCreateNote: () -> Unit,
    onLogout: () -> Unit
) {
    val screenModel: NotesScreenModel = koinInject()
    val notesState by screenModel.state.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val backgroundColor = Color(0xFFF5EDE0) // light beige
    val primaryBrown = Color(0xFF6D4C41)    // chocolate brown
    val accentBrown = Color(0xFF8D6E63)     // lighter brown

    // Filter notes based on search query
    val filteredNotes = when (val state = notesState.notes) {
        is ResultState.Success -> {
            state.data.filter {
                it.title?.contains(searchQuery, ignoreCase = true) == true ||
                        it.content?.contains(searchQuery, ignoreCase = true) == true
            }
        }
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Noted", color = backgroundColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryBrown),
                actions = {
                    TextButton(
                        onClick = {
                            screenModel.logout()
                                onLogout()
                        }
                    ) {
                        Text("Logout", color = backgroundColor)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNote,
                containerColor = accentBrown,
                contentColor = Color.White
            ) { Text("+", color = backgroundColor) }
        },
        containerColor = primaryBrown
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            Surface(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search notes...") },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrown,
                        focusedLabelColor = primaryBrown
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                    // colors parameter is not supported in KMP
                )
            }


            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = notesState.notes) {
                    is ResultState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primaryBrown
                    )
                    is ResultState.Failure -> Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    is ResultState.Success -> {
                        if (filteredNotes.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No notes found", style = MaterialTheme.typography.titleMedium, color = backgroundColor)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredNotes) { note ->
                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onNoteClick(note) },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    note.title ?: "",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    maxLines = 1,
                                                    color = primaryBrown,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "❌",
                                                    color = Color.Red,
                                                    modifier = Modifier
                                                        .clickable { screenModel.deleteNote(note.id ?: 0) }
                                                        .padding(8.dp)
                                                )
                                            }
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                note.content ?: "",
                                                style = MaterialTheme.typography.bodyMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = accentBrown
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Load notes once on screen start
    LaunchedEffect(Unit) { screenModel.loadNotes() }
}


