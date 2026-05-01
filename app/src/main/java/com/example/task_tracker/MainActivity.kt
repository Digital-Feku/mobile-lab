package com.example.task_tracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ScheduleTheme {
                ScheduleApp()
            }
        }
    }
}

@Composable
fun ScheduleTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()

    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> {
            dynamicDarkColorScheme(context)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !darkTheme -> {
            dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme()

        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun ScheduleApp() {
    val lesson1Time = stringResource(R.string.lesson_1_time)
    val lesson1Subject = stringResource(R.string.lesson_1_subject)
    val lesson1Room = stringResource(R.string.lesson_1_room)

    val lesson2Time = stringResource(R.string.lesson_2_time)
    val lesson2Subject = stringResource(R.string.lesson_2_subject)
    val lesson2Room = stringResource(R.string.lesson_2_room)

    val lesson3Time = stringResource(R.string.lesson_3_time)
    val lesson3Subject = stringResource(R.string.lesson_3_subject)
    val lesson3Room = stringResource(R.string.lesson_3_room)

    val newLessonTime = stringResource(R.string.new_lesson_time)
    val roomNotSpecified = stringResource(R.string.room_not_specified)

    val lessons = remember {
        mutableStateListOf(
            Lesson(
                time = lesson1Time,
                subject = lesson1Subject,
                room = lesson1Room
            ),
            Lesson(
                time = lesson2Time,
                subject = lesson2Subject,
                room = lesson2Room
            ),
            Lesson(
                time = lesson3Time,
                subject = lesson3Subject,
                room = lesson3Room
            )
        )
    }

    var newLessonSubject by remember {
        mutableStateOf("")
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.schedule_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = pluralStringResource(
                    R.plurals.lesson_count,
                    lessons.size,
                    lessons.size
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
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
                        Text(stringResource(R.string.lesson_input_label))
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
                                    time = newLessonTime,
                                    subject = newLessonSubject.trim(),
                                    room = roomNotSpecified
                                )
                            )

                            newLessonSubject = ""
                        }
                    }
                ) {
                    Text(stringResource(R.string.add_lesson_short))
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
                    text = stringResource(R.string.time_label, lesson.time)
                )

                Text(
                    text = stringResource(R.string.room_label, lesson.room)
                )
            }

            Button(
                onClick = onDelete
            ) {
                Text(stringResource(R.string.delete_lesson))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleAppPreview() {
    ScheduleTheme {
        ScheduleApp()
    }
}