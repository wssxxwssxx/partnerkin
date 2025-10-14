package com.example.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.example.project.conference.data.model.Category
import org.example.project.conference.data.model.Conference
import org.example.project.conference.presentation.ConferenceViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConferenceListScreen(
    viewModel: ConferenceViewModel = koinViewModel(),
    onConferenceClick: (Conference) -> Unit
) {
    val conferences by viewModel.conferences.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadConferences() }

    Column(Modifier.fillMaxSize()) {
        ConferenceTopBar(
            onBackClick = { /* обработка назад */ },
            onSupportClick = { /* обработка поддержки */ }
        )

        when {
            isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Text("Ошибка: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
            else -> {
                val grouped = conferences.groupBy { extractMonthYear(it.startDate) }
                LazyColumn(modifier = Modifier
                    .background(Color(0xFFFFFFFF))) {
                    grouped.entries.forEachIndexed  { index, (monthYear, confList) ->
                        item {
                            Text(
                                text = monthYear,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                                modifier = Modifier.padding(start = 16.dp, if (index == 0) 16.dp else 48.dp, bottom = 24.dp)
                            )
                        }
                        items(confList) { conference ->
                            ConferenceCard(
                                conference = conference,
                                onClick = { onConferenceClick(conference) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Пример утилиты - выводим "Март 2025" **/
fun extractMonthYear(date: String): String {
    return try {
        val parts = date.split("-") // "2025-03-10"
        val month = when(parts[1].toInt()) {
            1 -> "Январь"
            2 -> "Февраль"
            3 -> "Март"
            4 -> "Апрель"
            5 -> "Май"
            6 -> "Июнь"
            7 -> "Июль"
            8 -> "Август"
            9 -> "Сентябрь"
            10 -> "Октябрь"
            11 -> "Ноябрь"
            12 -> "Декабрь"
            else -> ""
        }
        "$month ${parts[0]}"
    } catch(e: Exception) { "" }
}

@Composable
fun ConferenceTopBar(
    title: String = "Конференции",
    onBackClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    Column(modifier = Modifier
        .background(Color(0xFFFFFFFF))
        .statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 4.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // своя иконка назад!
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0E1234),
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .weight(10f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onSupportClick, modifier = Modifier.padding(end = 4.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

    }
}

@Composable
fun ConferenceCard(
    conference: Conference,
    onClick: () -> Unit
) {
    val status = conference.status == "canceled"
    val bgColor = when (status) {
        true -> Color(0xFFFFEFEF)
        else -> Color(0xFFEFF2F9)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            if (status) {
                CanceledChip()
                Spacer(Modifier.height(26.dp))
            }

            Text(
                text = conference.name,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                color = Color(0xFF060A3C),
                modifier = Modifier.alpha(if (status) 0.6f else 1f),
                fontSize = 24.sp, // тут задаём явный размер!
            )
            Spacer(Modifier.height(20.dp))

            ConferenceImageAndDate(
                imageUrl = conference.image?.url,
                status = status,
                startDate = conference.startDate,
                endDate = conference.endDate
            )
            Spacer(Modifier.height(24.dp))

            CategoryRow(status, compact = true, categories = conference.categories)

            LocationText(conference.country, conference.city, status)

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CanceledChip() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFFF3333),
                shape = RoundedCornerShape(60.dp)
            )
            .background(Color(0xFFFFEFEF), shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_lightning),
            contentDescription = null,
            tint = Color(0xFFFF3333),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Отменено",
            color = Color(0xFFFF3333),
            fontSize = 11.sp,
            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
        )
    }
}


@Composable
fun LocationText(
    country: String,
    city: String?,
    status: Boolean,
    isOnline: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
            .alpha(if (status) 0.6f else 1f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = null,
            tint = Color(0xFF060A3C), // тёмный синий
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isOnline) "Онлайн" else listOfNotNull(city, country).joinToString(", "),
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 14.sp,
            color = Color(0xFF060A3C)
        )
    }
}

@Composable
fun ConferenceImageAndDate(
    imageUrl: String?,
    status: Boolean,
    startDate: String,
    endDate: String
) {
    val bgColor = when (status) {
        true -> Color(0xFFFFE6E6)
        else -> Color(0xFFE9EDF9)
    }

    Row(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .height(104.dp)
            .alpha(if (status) 0.6f else 1f)
            .fillMaxWidth()
    ) {
        val imageShape = RoundedCornerShape(
            topStart = 12.dp,
            bottomStart = 12.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp
        )

        Box(
            modifier = Modifier
                .width(156.dp)
                .fillMaxHeight()
                .clip(imageShape)
                .background(Color.White, imageShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null && imageUrl.isNotBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(imageShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .background(Color.Transparent, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "${formatDaySingle(startDate)} - ${formatDaySingle(endDate)}",
                    fontFamily = FontFamily(Font(R.font.inter_light)),
                    color = Color(0xFF0E1234),
                    fontSize = 40.sp, // тут задаём явный размер!
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Центрируем месяц по горизонтали!
                ) {
                    Text(
                        text = formatMonthShort(startDate),
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontSize = 14.sp,
                        color = Color(0xFF0E1234),
                    )
                    Spacer(Modifier.width(53.dp))
                    Text(
                        text = formatMonthShort(endDate),
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontSize = 14.sp,
                        color = Color(0xFF0E1234),
                    )
                }
            }
        }

    }
}

fun formatDaySingle(date: String): String {
    return date.split("-").getOrNull(2) ?: ""
}

fun formatMonthShort(date: String): String {
    val month = date.split("-")[1].toIntOrNull()
    return when(month) {
        1 -> "JAN"
        2 -> "FEB"
        3 -> "MAR"
        4 -> "APR"
        5 -> "MAY"
        6 -> "JUN"
        7 -> "JUL"
        8 -> "AUG"
        9 -> "SEP"
        10 -> "OCT"
        11 -> "NOV"
        12 -> "DEC"
        else -> ""
    }
}

@Composable
fun CategoryRow(status: Boolean, compact: Boolean, categories: List<Category>) {
    Row(Modifier.padding(top = if (compact) 0.dp else 6.dp)) {
        categories.forEach {
            Text(
                text = it.name,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontSize = 11.sp,
                color = Color(0xFF060A3C),
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(24.dp))
                    .alpha(if (status) 0.6f else 1f)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
            Spacer(Modifier.width(8.dp))
        }
    }
}
