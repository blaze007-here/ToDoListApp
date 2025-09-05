package com.vg.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class TodoItem(
    val id: Int,
    val text: String,
    val isDone: MutableState<Boolean> = mutableStateOf(false)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

@Composable
fun TodoApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "My Simple To-Do",
                style = MaterialTheme.typography.titleLarge   // ✅ Material 3 style
            )
            Spacer(modifier = Modifier.height(8.dp))
            TodoScreen()
        }
    }
}

@Composable
fun TodoScreen() {
    val todos = remember { mutableStateListOf<TodoItem>() }
    var text by remember { mutableStateOf("") }
    var nextId by remember { mutableStateOf(0) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Add task") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (text.isNotBlank()) {
                todos.add(TodoItem(nextId++, text))
                text = ""
            }
        }) {
            Text("Add")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn {
        items(todos, key = { it.id }) { item ->
            TodoRow(
                item = item,
                onDelete = { todos.remove(item) },
                onToggle = { item.isDone.value = !item.isDone.value }
            )
            Divider()
        }
    }
}

@Composable
fun TodoRow(item: TodoItem, onDelete: () -> Unit, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isDone.value,
            onCheckedChange = { onToggle() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = item.text,
            style = if (item.isDone.value)
                TextStyle(textDecoration = TextDecoration.LineThrough)
            else
                TextStyle(),
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoApp()
}
