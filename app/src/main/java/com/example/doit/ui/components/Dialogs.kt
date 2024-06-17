package com.example.doit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.doit.data.NoteEntity


@Composable
fun NoteInputDialog(
    dialogType:String,
    onDismiss:()->Unit,
    onSave:(NoteEntity)->Unit,
    note: NoteEntity = NoteEntity(id=0,"","",false)){
    var input by rememberSaveable(
        saver = listSaver<MutableState<NoteEntity>,Any>(
                save = {listOf(it.value.id,it.value.title,it.value.description) },
            restore = { mutableStateOf(NoteEntity(id=it[0] as Int, title = it[1] as String, description = it[2] as String))}
    )){
        mutableStateOf(note)
    }
    Dialog(onDismissRequest = {} ) {
        Card(
            modifier = Modifier.height(250.dp)
        ){
            Column(
                modifier= Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = dialogType, textAlign = TextAlign.Justify, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top=8.dp,bottom=8.dp))
                OutlinedTextField(
                    value = input.title,
                    onValueChange = { title->
                        input=input.copy(
                            title=title
                        )
                    },
                    label = {
                        Text(text = "Title")
                    },
                    maxLines = 1
                )
                OutlinedTextField(
                    value = input.description,
                    onValueChange = {desc->
                        input= input.copy(
                            description = desc
                        )
                    },
                    label = {
                        Text(text = "Description")
                    },
                    modifier = Modifier.fillMaxHeight()
                )
                Row (
                    modifier = Modifier.align(alignment = Alignment.End)
                ){
                    TextButton(onClick = {
                        onSave(NoteEntity(id=input.id,title=input.title,description = input.description))
                    }

                    ) {
                        Text(text = "save")
                    }
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun NoteDeleteDialog(onDismiss: () -> Unit,onConfirm:()->Unit,input:String){
    AlertDialog(
        onDismissRequest =onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(text = "confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "cancel")
            }
        },
        title = {
            Text(text = "Confirm Delete")
        },
        text = {
            Text(text = "Are you sure to delete all your $input? Doing so will delete them permanently.")
        }
    )
}
