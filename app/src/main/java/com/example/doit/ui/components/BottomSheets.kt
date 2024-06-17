package com.example.doit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.data.NoteEntity
import com.example.doit.ui.theme.DoItTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteBottomSheet(
    sheetState: SheetState,
    onSave:(NoteEntity)->Unit,
    onDelete: (NoteEntity) -> Unit,
    onDoneDelete: () -> Unit,
    markDone: () -> Unit,
    markUndone: () -> Unit,
    onDismiss:()->Unit,
    noteEntity: NoteEntity,
    completed:Boolean,
    modifier: Modifier=Modifier){
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState= sheetState
    ){
        SheetContent(noteEntity = noteEntity,onDismiss=onDismiss,onSave=onSave,onDoneDelete=onDoneDelete,completed=completed,onDelete=onDelete,markDone=markDone,markUndone=markUndone,modifier=modifier)
    }
}
@Composable
fun SheetContent(
    noteEntity:NoteEntity,
    onDismiss: () -> Unit,
    onSave:(NoteEntity)->Unit,
    onDelete:(NoteEntity)->Unit,
    onDoneDelete:()->Unit,
    markDone:()->Unit,
    completed: Boolean,
    markUndone:()->Unit,
    modifier: Modifier=Modifier){
    var input by rememberSaveable(
        saver = listSaver<MutableState<NoteEntity>,Any>(
            save = {listOf(it.value.id,it.value.title,it.value.description) },
            restore = { mutableStateOf(NoteEntity(id=it[0] as Int, title = it[1] as String, description = it[2] as String))}
        )){
        mutableStateOf(noteEntity)
    }
    if(!completed) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedButton(
                    onClick = {
                        onSave(input)
                        onDismiss()
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Save")
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDelete(input)
                            onDismiss()
                        }
                    ) {
                        Text(text = "Delete")
                    }
                    OutlinedButton(onClick = {
                        markDone()
                        onDismiss()
                    }) {
                        Text(text = "Mark as Done")
                    }
                }
            }
            TextField(
                value = input.title,
                onValueChange = { title -> input = input.copy(title = title) },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth(),
                label = {
                    Text(text = "Title")
                }
            )
            TextField(
                value = input.description,
                onValueChange = { desc -> input = input.copy(description = desc) },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth(),
                label = {
                    Text(text = "Description")
                }
            )

        }
    }
    else{
        Column(modifier=Modifier.padding(4.dp)) {
            OutlinedButton(
                onClick = {
                    markUndone()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Mark as TODO")
            }
            OutlinedButton(
                onClick = {
                    onDoneDelete()
                    onDismiss()
                },
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Delete")
            }
        }
    }
        Spacer(modifier = Modifier.height(100.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrevComps(){
    val sheetState = rememberModalBottomSheetState()
    DoItTheme {
    }
}