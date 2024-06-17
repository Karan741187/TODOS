package com.example.doit.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.util.TypedValueCompat.pxToDp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doit.data.AppViewModelProvider
import com.example.doit.data.DoneEntity
import com.example.doit.data.NoteEntity
import com.example.doit.ui.theme.DoItTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(notesViewModel: NotesViewModel,modifier: Modifier=Modifier){
    val noteList by notesViewModel.notes.collectAsState()
    val doneList by notesViewModel.dones.collectAsState()
    val stateList = remember{ derivedStateOf {noteList.toMutableStateList()}}
    val doneStateList= remember{ derivedStateOf { doneList.toMutableStateList() }}
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

     Column(
         modifier=Modifier.padding(top=4.dp)
     ){
        if (noteList.isNotEmpty() or doneList.isNotEmpty()) {
            NotesList(
                onNoteClick = {index->
                      val temp = stateList.value[index]
                    stateList.value[index]=temp.copy(state = !(temp.state))
                },
                modifier = modifier,
                notes=stateList.value,
                dones = doneStateList.value,
                onNoteLongClick = {index->
                    notesViewModel.completed = false
                    notesViewModel.deleteIndex=index
                    notesViewModel.showSheet =true
                },
                onDoneClick = {index->
                    val temp = doneStateList.value[index]
                          doneStateList.value[index]= temp.copy(state = !(temp.state))
                },
                onDoneLongClick = {index->
                    notesViewModel.completed =true
                    notesViewModel.deleteIndex = index
                    notesViewModel.showSheet=true
                },
                onDone = {index->
                    val temp= stateList.value[index]
                    stateList.value.removeAt(index)
                    doneStateList.value.add(DoneEntity(temp.id,temp.title,temp.description,temp.state))
                    scope.launch {
                        notesViewModel.removeNote(temp)
                    }
                    scope.launch {
                        notesViewModel.saveDone(DoneEntity(temp.id,temp.title,temp.description,false))
                    }
                },
                showCompletes = notesViewModel.showCompletes,
                toggleCompletes = {
                    notesViewModel.showCompletes = !(notesViewModel.showCompletes)
                }
            )
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "You're all done!")
            }
        }

        AnimatedVisibility(notesViewModel.takeInput) {
            NoteInputDialog(
                dialogType = "Enter your note",
                onDismiss = { notesViewModel.takeInput = false },
                onSave = {note->
                    notesViewModel.takeInput=false
                    scope.launch {
                        notesViewModel.saveNote(note)
                        notesViewModel.snackbarHostState.showSnackbar("Note saved successfully!")
                    }
                }
            )
        }
        AnimatedVisibility (notesViewModel.deleteNote) {
            NoteDeleteDialog(
                onDismiss = {notesViewModel.deleteNote=false},
                onConfirm = {
                    scope.launch {
                        notesViewModel.clearTODOS()
                        notesViewModel.snackbarHostState.showSnackbar("All TODOs cleared!")
                    }
            },
                input = "TODOs"
            )

        }
        AnimatedVisibility (notesViewModel.deleteDone) {
            NoteDeleteDialog(
                onDismiss = {notesViewModel.deleteDone=false},

                onConfirm = {
                    scope.launch {
                        notesViewModel.clearDones()
                        notesViewModel.snackbarHostState.showSnackbar("All completes cleared!")
                    }
                },
                input = "completed tasks"
            )

        }

        AnimatedVisibility(
            visible = notesViewModel.showSheet
        ) {
            NoteBottomSheet(
                sheetState = sheetState,
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) notesViewModel.showSheet = false
                    }
                },
                onSave = {note->
                    scope.launch{
                        notesViewModel.updateNote(note)
                        notesViewModel.snackbarHostState.showSnackbar("Note updated successfully!")
                    }

                },
                onDelete = {note->
                           scope.launch {
                               notesViewModel.removeNote(note)
                               notesViewModel.snackbarHostState.showSnackbar("Note deleted successfully!")
                           }
                },
                markDone = {
                           val index=notesViewModel.deleteIndex
                           if(index>-1 && index < noteList.size){
                               val temp= stateList.value[index]
                               stateList.value.removeAt(index)
                               doneStateList.value.add(DoneEntity(temp.id,temp.title,temp.description,temp.state))
                               scope.launch {
                                   notesViewModel.removeNote(temp)
                               }
                               scope.launch {
                                   notesViewModel.saveDone(DoneEntity(temp.id,temp.title,temp.description,false))
                               }
                           }
                },
                markUndone = {
                             val index = notesViewModel.deleteIndex
                        if(index>-1 && index < doneList.size){
                            val temp = doneStateList.value[index]
                            val note=NoteEntity(temp.id,temp.title,temp.description,temp.state)
                            doneStateList.value.removeAt(index)
                            stateList.value.add(note)
                            scope.launch { notesViewModel.removeDone(temp) }
                            scope.launch { notesViewModel.saveNote(note.copy(state=false)) }
                        }
                },
                noteEntity = if(noteList.size>notesViewModel.deleteIndex) noteList[notesViewModel.deleteIndex] else NoteEntity(0,"","",false),
                modifier=modifier,
                completed = notesViewModel.completed,
                onDoneDelete = {
                    val index = notesViewModel.deleteIndex
                    if(index>-1 && index < doneList.size){
                        scope.launch {
                            notesViewModel.removeDone(doneList[index])
                            notesViewModel.snackbarHostState.showSnackbar("Completed task removed!")
                        }
                    }
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(){
    val notesViewModel:NotesViewModel= viewModel(factory= AppViewModelProvider.Factory)
    val scope = rememberCoroutineScope()


    Box(
        contentAlignment = Alignment.TopEnd
    ){
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = notesViewModel.snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    notesViewModel.takeInput = true
                }) {
                    Icon(Icons.Filled.Create, contentDescription = "Add a note")
                }
            },
            topBar = {

                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "TODO",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 8.dp, bottom = 0.dp)
                        )

                    },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    actions = {
                        IconButton(
                            onClick = { notesViewModel.showMenu = !(notesViewModel.showMenu) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Options"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer)
                )
            }
        ) {
            NotesScreen(notesViewModel = notesViewModel,modifier=Modifier.padding(it))
        }
        AnimatedVisibility(visible = notesViewModel.showMenu) {
            Menu(
                onDismiss = {notesViewModel.showMenu = !(notesViewModel.showMenu)},
                onClearTODOS = {
                    notesViewModel.showMenu = false
                    scope.launch { notesViewModel.deleteNote = true }
                },
                onClearDones = {
                    notesViewModel.showMenu=false
                    scope.launch { notesViewModel.deleteDone = true }
                }
            )
        }
    }
}


@Composable
fun Menu(onDismiss:()->Unit,onClearTODOS:()->Unit,onClearDones:()->Unit,modifier: Modifier=Modifier){
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss,
        modifier=modifier
            .padding(end=4.dp),
    ){
        DropdownMenuItem(text = { Text(text = "Clear TODOs")}, onClick = onClearTODOS)
        DropdownMenuItem(text = { Text(text = "Clear Completed tasks")}, onClick = onClearDones)
    }
}


@Preview(showSystemUi = true)
@Composable
fun CheckComps(){
    DoItTheme {

    }
}