package com.example.task_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Lesson(
    val time: String,
    val subject: String,
    val room: String,
    val completed: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScheduleApp()
        }
    }
}

@Composable
fun ScheduleApp() {
    MaterialTheme {
        val lessons = remember {
            mutableStateListOf(
                Lesson(
                    time = "09:00",
                    subject = "Основы Kotlin",
                    room = "204"
                ),
                Lesson(
                    time = "10:40",
                    subject = "Jetpack Compose",
                    room = "305"
                ),
                Lesson(
                    time = "12:20",
                    subject = "Мобильная разработка",
                    room = "210"
                )
            )
        }

        var newLessonSubject by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Расписание занятий",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newLessonSubject,
                    onValueChange = {
                        newLessonSubject = it
                    },
                    label = {
                        Text("Название занятия")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = {
                        if (newLessonSubject.isNotBlank()) {
                            lessons.add(
                                Lesson(
                                    time = "Новая",
                                    subject = newLessonSubject.trim(),
                                    room = "Не указана"
                                )
                            )

                            newLessonSubject = ""
                        }
                    }
                ) {
                    Text("+")
                }
            }

            LazyColumn {
                itemsIndexed(lessons) { index, lesson ->
                    LessonElement(
                        lesson = lesson,
                        onCompletedChange = { checked ->
                            lessons[index] = lesson.copy(completed = checked)
                        },
                        onDelete = {
                            lessons.removeAt(index)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LessonElement(
    lesson: Lesson,
    onCompletedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = lesson.completed,
                onCheckedChange = onCompletedChange
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = lesson.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (lesson.completed) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                Text(
                    text = "Время: ${lesson.time}"
                )

                Text(
                    text = "Аудитория: ${lesson.room}"
                )
            }

            Button(
                onClick = onDelete
            ) {
                Text("Удалить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleAppPreview() {
    ScheduleApp()
}