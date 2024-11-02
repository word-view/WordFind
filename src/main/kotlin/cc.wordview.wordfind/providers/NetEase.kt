package cc.wordview.wordfind.providers

import cc.wordview.wordfind.exception.LyricsNotFoundException
import cc.wordview.wordfind.extensions.getOrThrow
import com.google.gson.JsonParser
import java.net.URLEncoder

// This implementation is adapted from syncedlyrics
// (https://github.com/moehmeni/syncedlyrics/blob/main/syncedlyrics/providers/netease.py)
class NetEase : Provider() {
    override val rootUrl = "https://music.163.com/api"

    private val netEaseCookies = "NMTID=00OFBaIzDVpFhEu9Ewanm5pUaO6_OwAAAGS4sn6QQ; JSESSIONID-WYYY=KliUhX8rseCVeoT2346cpua8w0oW%2BBk7lBcEieCSGipmcfih3mAYByp9g%2B7xOMcPlrDC6IIr8m1xv2o5wUlEhdxE0PB4KqqCvRH6T7%2B8%5CGCulUXSDVvGEjmeO%2FRlhtxhAD0WHsmMGOVW0uCJtq%2F%2B4DBaag%5CzVoD7xMKHrjWWF2AtD2kf%3A1730383542313; _iuqxldmzr_=32; _ntes_nnid=577aa8d915b4b500b9f86a72f76027a4,1730381742354; _ntes_nuid=577aa8d915b4b500b9f86a72f76027a4; WEVNSM=1.0.0; WNMCID=lblucg.1730381745839.01.0; WM_NI=g60kO%2F2B93F%2FFhxJySOV9ejtamu5%2BCS8fx0sbdKCKLDN13nWFGiKQ3JjwN2iELdWQVVueN9FtQvvIE70aGIQfme61%2FAElX4h78D9ElqQhKO6tEd6S9F9%2FVm0I8dJ0%2FO8UEs%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee92e65fb58dfab8f26b8f928aa7c85a928e9aaccb448db887d6c961899de19bdb2af0fea7c3b92aa5ea008cce25f29ef7d4c52586b8b8abfc598d8af786e980a3b3bbd6aa4991e8aa99b379a2bc85a4ca488892ffd4ed5d8699a7acd4398ab6b9afb1638794acb8d45e96ababafbc4eb88fba97cc4ff5f0afa8cc5ba6b3abd7b425839ba4b9b764f5e88cdac868b38fa8acce3eb89bacd7d23386e7e1b7f03c8be8acabb3258de89ba6f237e2a3; WM_TID=bPqrDqPjXf5AAREUFVeDTO1Y4NgLXqzC"

    override fun find(trackName: String, artistName: String, albumName: String?, duration: Int?): String {
        var url = "$rootUrl/search/pc"

        val searchTerm = URLEncoder.encode("$trackName $artistName")

        url += "?limit=5&type=1&offset=0&s=$searchTerm"

        val request = request(netEaseCookies).url(url).build()
        val response = client.newCall(request).execute()

        val resultId = parseSearchResponse(response.body?.string()!!, response.code)

        val urlLyrics = "$rootUrl/song/lyric?id=$resultId&lv=1"

        val requestLyrics = request(netEaseCookies).url(urlLyrics).build()
        val responseLyrics = client.newCall(requestLyrics).execute()

        return parseResponse(responseLyrics.body?.string()!!, responseLyrics.code)
    }

    fun parseResponse(body: String, statusCode: Int): String {
        when (statusCode) {
            200 -> {
                val jsonObject = JsonParser.parseString(body).asJsonObject

                val lyrics = jsonObject
                    .getOrThrow("lrc").asJsonObject
                    .getOrThrow("lyric").asString!!

                return lyrics
            }
            404 -> throw LyricsNotFoundException("NetEase was unable to find any lyrics")
            else -> throw Exception("NetEase returned a unhandled status_code=${statusCode}")
        }
    }

    fun parseSearchResponse(body: String, statusCode: Int): Int {
        when (statusCode) {
            200 -> {
                val jsonObject = JsonParser.parseString(body).asJsonObject!!

                val songs = jsonObject
                    .getOrThrow("result").asJsonObject
                    .getOrThrow("songs").asJsonArray!!

                if (songs.size() == 0)
                    throw LyricsNotFoundException("Returned songs list is empty")

                val id = songs.get(0).asJsonObject.getOrThrow("id")

                return id.asInt
            }
            404 -> throw LyricsNotFoundException("NetEase was unable to find any lyrics")
            else -> throw Exception("NetEase returned a unhandled status_code=${statusCode}")
        }
    }
}