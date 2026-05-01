package com.example.task_tracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

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
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> dynamicDarkColorScheme(context)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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

    val lessonAddedText = stringResource(R.string.snackbar_lesson_added)
    val lessonDeletedText = stringResource(R.string.snackbar_lesson_deleted)
    val emptyInputText = stringResource(R.string.snackbar_empty_input)

    val lessons = remember {
        mutableStateListOf(
            Lesson(lesson1Time, lesson1Subject, lesson1Room),
            Lesson(lesson2Time, lesson2Subject, lesson2Room),
            Lesson(lesson3Time, lesson3Subject, lesson3Room)
        )
    }

    val lessonCountText = pluralStringResource(
        R.plurals.lesson_count,
        lessons.size,
        lessons.size
    )

    var newLessonSubject by remember { mutableStateOf("") }
    var lessonToDeleteIndex by remember { mutableStateOf<Int?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun addLesson() {
        if (newLessonSubject.isNotBlank()) {
            lessons.add(
                Lesson(
                    time = newLessonTime,
                    subject = newLessonSubject.trim(),
                    room = roomNotSpecified
                )
            )

            newLessonSubject = ""

            scope.launch {
                snackbarHostState.showSnackbar(
                    message = lessonAddedText,
                    duration = SnackbarDuration.Short
                )
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = emptyInputText,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.schedule_title))
                },
                actions = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = lessonCountText,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    ) {
                        Text(stringResource(R.string.info_action))
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addLesson()
                }
            ) {
                Text(stringResource(R.string.add_lesson_short))
            }
        }
    ) { contentPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 8.dp)
        ) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LessonInput(
                        value = newLessonSubject,
                        onValueChange = { newLessonSubject = it },
                        onAdd = { addLesson() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    LessonList(
                        lessons = lessons,
                        onCompletedChange = { index, checked ->
                            lessons[index] = lessons[index].copy(completed = checked)
                        },
                        onDeleteClick = { index ->
                            lessonToDeleteIndex = index
                        },
                        modifier = Modifier.weight(2f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LessonInput(
                        value = newLessonSubject,
                        onValueChange = { newLessonSubject = it },
                        onAdd = { addLesson() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    LessonList(
                        lessons = lessons,
                        onCompletedChange = { index, checked ->
                            lessons[index] = lessons[index].copy(completed = checked)
                        },
                        onDeleteClick = { index ->
                            lessonToDeleteIndex = index
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    val indexToDelete = lessonToDeleteIndex

    if (indexToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                lessonToDeleteIndex = null
            },
            title = {
                Text(stringResource(R.string.delete_dialog_title))
            },
            text = {
                Text(stringResource(R.string.delete_dialog_text))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (indexToDelete in lessons.indices) {
                            lessons.removeAt(indexToDelete)
                        }

                        lessonToDeleteIndex = null

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = lessonDeletedText,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm_delete_action))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        lessonToDeleteIndex = null
                    }
                ) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        )
    }
}

@Composable
fun LessonInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(stringResource(R.string.lesson_input_label))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_lesson_short))
            }
        }
    }
}

@Composable
fun LessonList(
    lessons: List<Lesson>,
    onCompletedChange: (Int, Boolean) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Text(
                text = pluralStringResource(
                    R.plurals.lesson_count,
                    lessons.size,
                    lessons.size
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        itemsIndexed(lessons) { index, lesson ->
            LessonElement(
                lesson = lesson,
                onCompletedChange = { checked ->
                    onCompletedChange(index, checked)
                },
                onDelete = {
                    onDeleteClick(index)
                }
            )
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

                Text(stringResource(R.string.time_label, lesson.time))
                Text(stringResource(R.string.room_label, lesson.room))
            }

            TextButton(
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