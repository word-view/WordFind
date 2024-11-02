@file:Suppress("DEPRECATION")

package cc.wordview.wordfind.providers

import cc.wordview.wordfind.exception.LyricsNotFoundException
import cc.wordview.wordfind.extensions.getOrThrow
import com.google.gson.JsonParser
import org.apache.commons.lang3.StringEscapeUtils
import java.net.URLEncoder

class MusixMatch : Provider() {
    override val rootUrl = "https://apic-desktop.musixmatch.com/ws/1.1"

    private val musixMatchCookies = "x-mxm-user-id=undefined; x-mxm-token-guid=undefined; mxm-encrypted-token=;"

    private var savedToken: String? = null
    private var tokenExpirationTime: Long? = null

    override fun find(trackName: String, artistName: String, albumName: String?, duration: Int?): String {
        val token = getToken()
        val trackId = search(trackName, artistName, token)
        val lyrics = getLyrics(trackId, token)

        return lyrics
    }

    fun getLyrics(trackId: String, token: String): String {
        val url = "$rootUrl/track.subtitle.get?track_id=$trackId&subtitle_format=lrc&usertoken=$token&app_id=web-desktop-app-v1.0"

        val request = request(musixMatchCookies).url(url).build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()!!

        return processGetLyrics(body, response.code)
    }

    fun processGetLyrics(body: String, statusCode: Int): String {
        when (statusCode) {
            200 -> {
                val jsonObject = JsonParser.parseString(body).asJsonObject!!

                val lyrics = jsonObject
                    .getOrThrow("message").asJsonObject
                    .getOrThrow("body").asJsonObject
                    .getOrThrow("subtitle").asJsonObject
                    .getOrThrow("subtitle_body").asString!!

                return StringEscapeUtils.unescapeJava(lyrics)
            }

            else -> throw Exception("MusixMatch returned a unhandled status_code=${statusCode}")
        }
    }

    fun search(trackName: String, artistName: String, token: String): String {
        val url =
            "$rootUrl/track.search?q=${URLEncoder.encode("$trackName $artistName")}&page_size=5&page=1&app_id=web-desktop-app-v1.0&usertoken=$token"

        val request = request(musixMatchCookies).url(url).build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()!!

       return processSearchResponse(body, response.code)
    }

    fun processSearchResponse(body: String, statusCode: Int): String {
        when (statusCode) {
            200 -> {
                val jsonObject = JsonParser.parseString(body).asJsonObject

                val tracksFound = jsonObject.getOrThrow("message").asJsonObject
                    .getOrThrow("body").asJsonObject
                    .getOrThrow("track_list").asJsonArray!!

                if (tracksFound.size() == 0)
                    throw LyricsNotFoundException("Returned track list is empty")

                val trackId = tracksFound.get(0).asJsonObject
                    .getOrThrow("track").asJsonObject
                    .getOrThrow("track_id").asString!!

                return trackId
            }
            else -> throw Exception("MusixMatch returned a unhandled status_code=${statusCode}")
        }
    }

    fun getToken(): String {
        if (savedToken != null && tokenExpirationTime != null) {
            if (tokenExpirationTime!! < System.currentTimeMillis()) return savedToken!!
        }

        val url = "$rootUrl/token.get?app_id=web-desktop-app-v1.0&user_language=en"

        val request = request(musixMatchCookies).url(url).build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()!!

       return processTokenResponse(body, response.code)
    }

    fun processTokenResponse(body: String, statusCode: Int): String {
        when (statusCode) {
            200 -> {
                val jsonObject = JsonParser.parseString(body).asJsonObject!!

                val token = jsonObject
                    .getOrThrow("message").asJsonObject
                    .getOrThrow("body").asJsonObject
                    .getOrThrow("user_token").asString!!

                savedToken = token
                tokenExpirationTime = System.currentTimeMillis().plus(600000) // 10 minutes

                return token
            }

            401 -> {
                // MusixMatch blocked us for some reason, as the priority is to give a lyrics
                // file as fast as possible we should ignore this and try the next provider.
                throw LyricsNotFoundException("MusixMatch rejected token request")
            }

            else -> throw Exception("MusixMatch returned a unhandled status_code=${statusCode}")
        }
    }
}