package cc.wordview.wordfind.providers

import cc.wordview.wordfind.exception.LyricsNotFoundException
import com.google.gson.JsonParser
import okhttp3.Request

class LrcLib : Provider() {
    override val rootUrl = "https://lrclib.net/api"

    override fun find(trackName: String, artistName: String, albumName: String?, duration: Int?): String {
        var url = "$rootUrl/get?track_name=$trackName&artist_name=$artistName"

        if (albumName != null) url += "&album_name=$albumName"
        if (duration != null) url += "&duration=$duration"

        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()

        return parseResponse(response.body?.string()!!, response.code)
    }

    fun parseResponse(body: String, statusCode: Int): String {
        when (statusCode) {
            200 -> {
                val jsonObject = JsonParser.parseString(body).asJsonObject!!

                val syncedLyricsElement = jsonObject.get("syncedLyrics")

                if (memberNotPresent(syncedLyricsElement))
                    throw LyricsNotFoundException("Lyrics are not synced")

                return syncedLyricsElement.asString!!
            }
            404 -> throw LyricsNotFoundException("LRCLIB was unable to find any lyrics")
            else -> throw Exception("LRCLIB returned a unhandled status_code=${statusCode}")
        }
    }
}