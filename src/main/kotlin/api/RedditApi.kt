package api

import com.google.gson.JsonParser
import helpers.LogsHelper
import helpers.NetHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object RedditApi {
    private val client = NetHelper.client

    private const val HEADER_NAME = "user-agent"
    private const val HEADER_VALUE = "ANTHONUS-Bot (https://github.com/ANTHONUSS/ANTHONUS-bot) by /u/Darkcp_YTB"

    fun getRandomImage(subreddit: String): String? {
        val url = "https://www.reddit.com/r/$subreddit/new.json?limit=100"

        val request = Request.Builder()
            .header(HEADER_NAME, HEADER_VALUE)
            .url(url)
            .build()

        val jsonData = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                LogsHelper.log.error("Response returned unexpected code", IOException("Unexpected code : $response"))
                return null
            }
            response.body.string()
        }

        try {
            val postsArray = JsonParser.parseString(jsonData)
                .asJsonObject
                .getAsJsonObject("data")
                .getAsJsonArray("children")

            val validImages = postsArray
                .map { it.asJsonObject.getAsJsonObject("data") }
                .filter { post ->
                    post.has("post_hint") && post.get("post_hint").asString == "image"
                            && post.has("is_video") && !post.get("is_video").asBoolean
                            && post.has("over_18") && !post.get("over_18").asBoolean
                            && post.has("url")
                }

            val randomPost = validImages.randomOrNull() ?: return null

            return randomPost.get("url").asString

        } catch (e: Exception) {
            LogsHelper.log.error("Error while parsing Json", e)
            return null
        }
    }
}