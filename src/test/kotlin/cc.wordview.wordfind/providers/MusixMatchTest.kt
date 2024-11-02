package cc.wordview.wordfind.providers

import cc.wordview.wordfind.exception.InvalidJsonException
import cc.wordview.wordfind.exception.LyricsNotFoundException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Ignore
import kotlin.test.assertEquals

class MusixMatchTest {
    val tokenResponse = "{\"message\":{\"body\":{\"user_token\": \"a_complex_user_token\"}}}"
    val emptyBodyResponse = "{\"message\":{\"body\":{}}}"
    val noBodyResponse = "{\"message\":{}}"

    val searchResponse = "{\"message\":{\"body\":{\"track_list\":[{\"track\":{\"track_id\": 64}},{}]}}}"
    val searchNoTrackIdResponse = "{\"message\":{\"body\":{\"track_list\":[{\"track\":{}},{}]}}}"
    val searchNoTrackResponse = "{\"message\":{\"body\":{\"track_list\":[{},{}]}}}"
    val searchTrackListEmptyResponse = "{\"message\":{\"body\":{\"track_list\":[]}}}"

    val lyricsResponse = "{\"message\":{\"body\":{\"subtitle\":{\"subtitle_body\":\"[00:06.74] hello\"}}}}"
    val lyricsUcodeResponse = "{\"message\":{\"body\":{\"subtitle\":{\"subtitle_body\":\"[00:06.74] \\u53ad\\u3089\\u3057\\u3044MAD\\u306a\\u30cd\\u30aa\\u30f3\\u306e\\u30b5\\u30a4\\u30f3\\u3067\"}}}}"
    val lyricsNoSubtitleBodyResponse = "{\"message\":{\"body\":{ \"subtitle\": {}}}}"

    @Test
    @Ignore("This test makes a request to the real API, only run it if something inside the find method changed that was not related to parseResponse")
    fun realSearch() {
        assertDoesNotThrow {
            val lyrics = MusixMatch().find("wonderland", "majiko")

            assertTrue(lyrics.isNotEmpty())
            assertTrue(lyrics.isNotBlank())
        }
    }

    @Test
    fun getToken() {
        assertDoesNotThrow {
            val token = MusixMatch().processTokenResponse(tokenResponse, 200)
            assertEquals("a_complex_user_token", token)
        }
    }

    @Test
    fun getTokenThrowsInvalid() {
        testGetTokenThrowsInvalid("{}")
        testGetTokenThrowsInvalid(emptyBodyResponse)
        testGetTokenThrowsInvalid(noBodyResponse)
    }

    @Test
    fun getTokenStatus401() {
        assertThrows<LyricsNotFoundException> {
            MusixMatch().processTokenResponse(noBodyResponse, 401)
        }
    }

    @Test
    fun getTokenAbnormalResponseCode() {
        assertThrows<Exception> {
            MusixMatch().processTokenResponse(noBodyResponse, 418)
        }
    }

    @Test
    fun search() {
        assertDoesNotThrow {
            val trackId = MusixMatch().processSearchResponse(searchResponse, 200)

            assertEquals("64", trackId)
        }
    }

    @Test
    fun searchThrowsInvalid() {
        testSearchThrowsInvalid(searchNoTrackIdResponse)
        testSearchThrowsInvalid(searchNoTrackResponse)
        testSearchThrowsInvalid(emptyBodyResponse)
        testSearchThrowsInvalid(noBodyResponse)
    }

    @Test
    fun searchTrackListEmpty() {
        assertThrows<LyricsNotFoundException> {
            MusixMatch().processSearchResponse(searchTrackListEmptyResponse, 200)
        }
    }

    @Test
    fun searchAbnormalResponseCode() {
        assertThrows<Exception> {
            MusixMatch().processSearchResponse("", 418)
        }
    }

    @Test
    fun getLyrics() {
        assertDoesNotThrow {
            val lyricsNormal = MusixMatch().processGetLyrics(lyricsResponse, 200)
            val lyricsUcode = MusixMatch().processGetLyrics(lyricsUcodeResponse, 200)

            assertEquals("[00:06.74] hello", lyricsNormal)
            assertEquals("[00:06.74] 厭らしいMADなネオンのサインで", lyricsUcode)
        }
    }

    @Test
    fun getLyricsAbnormalResponseCode() {
        assertThrows<Exception> {
            MusixMatch().processGetLyrics("", 418)
        }
    }

    @Test
    fun getLyricsInvalidJson() {
        testLyricsThrowsInvalid(lyricsNoSubtitleBodyResponse)
        testLyricsThrowsInvalid(emptyBodyResponse)
        testLyricsThrowsInvalid(noBodyResponse)
    }

    fun testLyricsThrowsInvalid(response: String) {
        assertThrows<InvalidJsonException> {
            MusixMatch().processGetLyrics(response, 200)
        }
    }

    fun testSearchThrowsInvalid(response: String) {
        assertThrows<InvalidJsonException> {
            MusixMatch().processSearchResponse(response, 200)
        }
    }

    fun testGetTokenThrowsInvalid(response: String) {
        assertThrows<InvalidJsonException> {
            MusixMatch().processTokenResponse(response, 200)
        }
    }
}