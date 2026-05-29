package dev.atick.compose // تأكد من مطابقة اسم الحزمة

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. Data Classes (القوالب التي سنستقبل فيها البيانات من الإنترنت)
data class PrayerResponse(val data: PrayerData)
data class PrayerData(val timings: PrayerTimings)
data class PrayerTimings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String
)

// 2. API Interface (أوامر الاتصال بالسيرفر)
interface PrayerApiService {
    @GET("v1/timingsByCity")
    suspend fun getTimings(
        @Query("city") city: String = "Homs", // تم ضبط حمص كمدينة افتراضية
        @Query("country") country: String = "Syria",
        @Query("method") method: Int = 5 // طريقة الحساب (الهيئة العامة للمساحة)
    ): PrayerResponse
}

// 3. Retrofit Instance (المحرك الذي ينفذ الاتصال)
object RetrofitClient {
    private const val BASE_URL = "https://api.aladhan.com/"

    val apiService: PrayerApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerApiService::class.java)
    }
}