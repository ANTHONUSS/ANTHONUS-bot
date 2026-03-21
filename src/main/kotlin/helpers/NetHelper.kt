package helpers

import okhttp3.OkHttpClient
import okhttp3.Request

object NetHelper {
    private val client = OkHttpClient()

    fun downloadImage(url: String): ByteArray {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Erreur HTTP: ${response.code}")
            return response.body.bytes()
        }
    }
}