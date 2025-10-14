package com.example.demo

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.example.project.conference.data.model.Category
import org.example.project.conference.data.model.ConferenceDetail
import org.example.project.conference.presentation.ConferenceDetailViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@Composable
fun ConferenceDetailScreen(
    conferenceId: String,
    onBackClick: () -> Unit,
    viewModel: ConferenceDetailViewModel = koinViewModel()
) {
    LaunchedEffect(conferenceId) {
        viewModel.loadConferenceDetail(conferenceId)
    }
    val detail by viewModel.conferenceDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    fun formatConferenceDate(start: String, end: String): String {
        val formatterSrc = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterDest = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
        val startDate = LocalDate.parse(start, formatterSrc)
        val endDate = LocalDate.parse(end, formatterSrc)
        val days = ChronoUnit.DAYS.between(startDate, endDate) + 1
        val dayText = when {
            days == 1L -> "1 день"
            days in 2..4 -> "$days дня"
            else -> "$days дней"
        }
        val formatted = startDate.format(formatterDest)
        return "$formatted, $dayText"
    }

    @Composable
    fun CategoryRow(categories: List<Category>) {
        Row(Modifier.padding(start = 16.dp, top = 20.dp)) {
            categories.forEach {
                Text(
                    text = it.name,
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontSize = 11.sp,
                    color = Color(0xFF0E1234),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFEFF2F9), RoundedCornerShape(24.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
        }
    }

    @Composable
    fun ConferenceDateLocation(
        dateText: String,
        locationText: String
    ) {
        Column(
            modifier = Modifier.padding(top = 20.dp, start = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_schedule),
                    contentDescription = null,
                    tint = Color(0xFF456AEF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = dateText,
                    color = Color(0xFF060A3C),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_blue),
                    contentDescription = null,
                    tint = Color(0xFF456AEF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = locationText,
                    color = Color(0xFF060A3C),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }

    @Composable
    fun RegistrationButton(registerUrl: String) {
        val context = LocalContext.current
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registerUrl))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .padding(top = 28.dp, start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF456AEF),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Регистрация",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            if (isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            } else if (error != null) {
                Box(Modifier.padding(16.dp)) {
                    Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
                }
            } else detail?.let { d ->
                Text(
                    text = "Конференция",
                    color = Color(0xFF0E1234),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    modifier = Modifier
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                )

                Text(
                    text = d.name,
                    color = Color(0xFF0E1234),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
                )

                // баннер конференции
                if (d.image?.url != null) {
                    AsyncImage(
                        model = d.image?.url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                            .clip(RoundedCornerShape(22.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                CategoryRow(d.categories)
                ConferenceDateLocation(
                    dateText = "${formatConferenceDate(d.startDate, d.endDate)}",
                    locationText = "${d.city ?: ""}${if (d.country != null) ", ${d.country}" else ""}"
                )

                if (!d.registerUrl.isNullOrEmpty()) {
                    RegistrationButton(d.registerUrl ?: "")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Описание (About)
                if (!d.about.isNullOrEmpty()) {
                    Text(
                        text = "О событии",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        color = Color(0xFF0E1234),
                        modifier = Modifier.padding(top = 28.dp,start = 16.dp)
                    )
                    Text(
                        text = d.about ?: "Null",
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = Color(0xFF0E1234),
                        modifier = Modifier.padding(top = 12.dp, start = 16.dp, bottom = 24.dp)
                    )
                }
                Spacer(Modifier.height(36.dp))
            }
        }
    }
}
