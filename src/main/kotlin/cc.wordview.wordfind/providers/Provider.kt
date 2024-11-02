package cc.wordview.wordfind.providers

import cc.wordview.wordfind.exception.InvalidJsonException
import cc.wordview.wordfind.exception.LyricsNotFoundException
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Defines a remote lyrics provider
 */
abstract class Provider {
    val client: OkHttpClient = OkHttpClient.Builder().build()

    abstract val rootUrl: String

    /**
     * User agent to be applied in the requests. Certain providers may only allow a "browser" to make the request.
     */
    protected val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0"

    /**
     * Function called to search for lyrics.
     * Should ideally delegate parsing the results to a separate function to keep
     * the external API from being fetched needlessly on tests. `parseResponse` will
     * not be defined in this interface as it is only used for tests.
     * ```kotlin
     * // How parse response should be defined
     * fun parseResponse(body: String, statusCode: Int): String
     * ```
     *
     * @param trackName The title of the music
     * @param artistName The name of the artist
     * @param albumName The name of the album
     * @param duration The duration (in seconds) of the music
     * @return The lyrics of the requested music
     */
    @Throws(LyricsNotFoundException::class, InvalidJsonException::class)
    abstract fun find(trackName: String, artistName: String, albumName: String? = null, duration: Int? = null): String


    /**
     * Checks if the jsonElement is `null` or `isJsonNull`, indicating if it's present in the json.
     *
     * @param jsonElement The json element pointing to a property.
     * @return If it's null in any form.
     */
    fun memberNotPresent(jsonElement: JsonElement?): Boolean {
        return jsonElement == null || jsonElement.isJsonNull
    }

    /**
     * Returns a preconfigured `Request.Builder` with cookies.
     *
     * @param cookie
     */
    fun request(cookie: String): Request.Builder {
        return Request.Builder().addHeader("User-Agent", USER_AGENT).addHeader("Cookie", cookie)
    }
}