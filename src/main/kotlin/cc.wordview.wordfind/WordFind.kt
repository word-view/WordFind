package cc.wordview.wordfind

import cc.wordview.wordfind.exception.InvalidJsonException
import cc.wordview.wordfind.exception.LyricsNotFoundException
import cc.wordview.wordfind.providers.Platform
import cc.wordview.wordfind.providers.Provider
import org.slf4j.LoggerFactory

/**
 * Provides access to lyrics platforms
 */
class WordFind {
    val logger = LoggerFactory.getLogger(this::class.java)
    val cache = mutableMapOf<String, String>()

    val platforms = arrayListOf<Platform>(
        Platform.MUSIXMATCH,
        Platform.LRCLIB,
        Platform.NETEASE,
    )

    /**
     * Tries to find the lyrics based on the given information.
     * It loops through all the predefined platforms until in finds something.
     *
     * @param trackName The name of the track
     * @param artistName The name of the artist
     * @param albumName The name of the album
     * @param duration The duration of the track
     * @param convertToVtt If it should be automatically returned in the VTT format
     * @return An LRC or VTT lyrics
     */
    @Throws(LyricsNotFoundException::class)
    fun find(
        trackName: String,
        artistName: String,
        convertToVtt: Boolean = false,
        albumName: String? = null,
        duration: Int? = null
    ): String {
        // Different exceptions are thrown purely for logging and debugging
        // because the action is the same always, to try again with the next platform.
        for (platform in platforms) {
            try {
                return find(trackName, artistName, platform.provider, convertToVtt, albumName, duration)
            } catch (e: LyricsNotFoundException) {
                logger.info("The platform ${platform.platformName} was unable to find any lyrics: ${e.message}")
                continue
            } catch (e: InvalidJsonException) {
                logger.error("The platform ${platform.platformName} returned a JSON that could not be parsed: ${e.message}")
                continue
            } catch (e: Exception) {
                logger.error("The platform ${platform.platformName} responded in a unexpected way: ${e.message}", e)
                continue
            }
        }

        throw LyricsNotFoundException("Could not find any lyrics in any platform: ${platforms.size} tried")
    }

    /**
     * Tries to find the lyrics based on the given information using the specified provider.
     *
     * @param trackName The name of the track
     * @param artistName The name of the artist
     * @param albumName The name of the album
     * @param duration The duration of the track
     * @param provider The lyrics provider
     * @param convertToVtt If it should be automatically returned in the VTT format
     * @return An LRC or VTT lyrics
     */
    @Throws(LyricsNotFoundException::class, InvalidJsonException::class)
    fun find(
        trackName: String,
        artistName: String,
        provider: Provider,
        convertToVtt: Boolean = false,
        albumName: String? = null,
        duration: Int? = null
    ): String {
        val key = trackName + artistName

        val lyrics = cache[key] ?: provider.find(trackName, artistName, albumName, duration)
        if (!cache.contains(key)) cache.put(key, lyrics)

        return if (convertToVtt) LrcToVtt().convert(lyrics) else lyrics
    }
}