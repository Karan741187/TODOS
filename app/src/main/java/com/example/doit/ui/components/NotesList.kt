package com.example.doit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.data.DoneEntity
import com.example.doit.data.NoteEntity
import com.example.doit.ui.theme.DoItTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesList(
    onDone: (Int) -> Unit,
    onDoneClick:(Int)->Unit,
    onDoneLongClick:(Int)->Unit,
    onNoteClick:(Int)->Unit,
    onNoteLongClick:(Int)->Unit,
    dones:SnapshotStateList<DoneEntity>,
    notes:SnapshotStateList<NoteEntity>,
    showCompletes:Boolean,
    toggleCompletes:()->Unit,
    modifier: Modifier=Modifier){
    LazyColumn(
        modifier = modifier
    ) {
        if(notes.size>0){
            itemsIndexed(notes, key = { _, item -> item.id }) { index, note ->
                ItemTile(
                    title = note.title,
                    note = note.description,
                    modifier = Modifier
                        .combinedClickable(onLongClick = {
                            onNoteLongClick(index)
                        }) {
                            onNoteClick(index)
                        },
                    isExpanded = note.state,
                    onDone = { onDone(index) },
                    completed = false
                )
            }
        }else{
            item{
                Text(
                    text = "All done !!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 0.dp, top = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically){
                Column(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp), thickness = 2.dp)
                    Text(text = "Completed", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary,modifier=Modifier.padding(start = 8.dp,bottom=4.dp))
                }
                IconButton(onClick = toggleCompletes) {
                    AnimatedVisibility (showCompletes) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "Show completed tasks"
                        )
                    }
                    AnimatedVisibility(visible = !showCompletes) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Hide completed tasks"
                        )
                    }
                }
            }
        }
        if(showCompletes) {
            itemsIndexed(dones, key = { _, done -> done.id }) { index, done ->
                ItemTile(
                    title = done.title,
                    note = done.description,
                    isExpanded = done.state,
                    completed = true,
                    modifier = Modifier
                        .combinedClickable(
                            onLongClick = {
                                onDoneLongClick(index)
                            }
                        ) { onDoneClick(index) }
                )
            }
        }
    }
}


@Composable
fun ItemTile(title:String,note:String,isExpanded:Boolean,completed:Boolean,onDone:()->Unit={},modifier: Modifier=Modifier){

    val surfaceColor by animateColorAsState(
        label = "Color Change",
        targetValue = if(isExpanded) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceVariant
    )
    ElevatedCard(
        modifier= modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
            .animateContentSize(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
    ){
        Row(
            modifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            if(!completed) RadioButton(selected = false, onClick = onDone)
            else Icon(imageVector = Icons.Filled.Done, contentDescription = "Task done")
            Column(modifier = if(completed) modifier.padding(start = 4.dp) else Modifier){
                Text(
                    text = title,
                    modifier = modifier
                        .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 2.dp),
                    style = if(!completed) MaterialTheme.typography.titleMedium else TextStyle(textDecoration = TextDecoration.LineThrough, fontWeight = FontWeight.Medium)
                )
                if(note.isNotEmpty()) {
                    Surface(
                        color = surfaceColor,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = note,
                            modifier = modifier.padding(4.dp),
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1
                        )
                    }
                }
            }
           
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CheckMyComps(){

    DoItTheme {
        ItemTile(title = "Title", note = "A description", isExpanded =false, completed = true)
    }
}