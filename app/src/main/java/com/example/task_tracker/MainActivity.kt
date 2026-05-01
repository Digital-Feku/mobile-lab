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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Lesson(
    val time: String,
    val subject: String,
    val room: String,
    val teacher: String
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
            listOf(
                Lesson(
                    time = "09:00",
                    subject = "Основы Kotlin",
                    room = "Ауд. 204",
                    teacher = "Иванов И.И."
                ),
                Lesson(
                    time = "10:40",
                    subject = "Jetpack Compose",
                    room = "Ауд. 305",
                    teacher = "Петров П.П."
                ),
                Lesson(
                    time = "12:20",
                    subject = "Мобильная разработка",
                    room = "Ауд. 210",
                    teacher = "Сидоров С.С."
                ),
                Lesson(
                    time = "14:00",
                    subject = "Базы данных",
                    room = "Ауд. 112",
                    teacher = "Кузнецова А.А."
                )
            )
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

            Text(
                text = "Лабораторная работа: основы разметки Compose",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(lessons) { lesson ->
                    LessonCard(lesson)
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: Lesson) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = lesson.time,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column {
                Text(
                    text = lesson.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(text = "Кабинет: ${lesson.room}")
                Text(text = "Преподаватель: ${lesson.teacher}")
            }
        }
    }
}