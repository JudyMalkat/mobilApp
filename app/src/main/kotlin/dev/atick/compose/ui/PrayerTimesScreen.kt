package dev.atick.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.atick.compose.R

@Composable
fun PrayerTimesScreen() {
    // متغيرات الحالة (State) لمراقبة التحميل والبيانات والأخطاء
    var timings by remember { mutableStateOf<PrayerTimings?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect ينفذ الكود البرمجي مرة واحدة عند فتح الشاشة (لجلب البيانات من النت)
    LaunchedEffect(Unit) {
        try {
            // الاتصال بالسيرفر وجلب الأوقات
            val response = RetrofitClient.apiService.getTimings()
            timings = response.data.timings
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "تأكد من اتصالك بالإنترنت"
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.prayer_times_title) + " - حمص",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // واجهة التحميل الذكية
        if (isLoading) {
            Spacer(modifier = Modifier.height(100.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("جاري جلب المواقيت الدقيقة...", style = MaterialTheme.typography.bodyLarge)
        } 
        // واجهة الخطأ (في حال انقطاع النت)
        else if (errorMessage != null) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleLarge)
        } 
        // واجهة النجاح العرض الفعلي للبيانات
        else if (timings != null) {
            // دمج نصوصنا المخزنة مع الأرقام القادمة من الإنترنت
            val prayerList = listOf(
                Pair(stringResource(R.string.prayer_fajr), formatTime(timings!!.Fajr)),
                Pair(stringResource(R.string.prayer_sunrise), formatTime(timings!!.Sunrise)),
                Pair(stringResource(R.string.prayer_dhuhr), formatTime(timings!!.Dhuhr)),
                Pair(stringResource(R.string.prayer_asr), formatTime(timings!!.Asr)),
                Pair(stringResource(R.string.prayer_maghrib), formatTime(timings!!.Maghrib)),
                Pair(stringResource(R.string.prayer_isha), formatTime(timings!!.Isha))
            )

            LazyColumn {
                items(prayerList.size) { index ->
                    val prayer = prayerList[index]
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = prayer.first, style = MaterialTheme.typography.titleMedium)
                            Text(text = prayer.second, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

// دالة هندسية صغيرة لتحويل نظام 24 ساعة القادم من السيرفر إلى نظام 12 ساعة المريح للقراءة
fun formatTime(time24: String): String {
    return try {
        val parts = time24.split(":")
        var hour = parts[0].toInt()
        val minute = parts[1]
        val ampm = if (hour >= 12) "م" else "ص"
        if (hour > 12) hour -= 12
        if (hour == 0) hour = 12
        "$hour:$minute $ampm"
    } catch (e: Exception) {
        time24
    }
}