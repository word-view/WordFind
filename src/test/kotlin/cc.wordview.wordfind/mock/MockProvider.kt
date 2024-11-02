package cc.wordview.wordfind.mock

import cc.wordview.wordfind.exception.InvalidJsonException
import cc.wordview.wordfind.exception.LyricsNotFoundException
import cc.wordview.wordfind.providers.Provider

class MockProvider : Provider() {
    override val rootUrl = "localhost:8080/api/v1"

    override fun find(
        trackName: String,
        artistName: String,
        albumName: String?,
        duration: Int?
    ): String {
        if (trackName == "throw notFound") {
            throw LyricsNotFoundException("Mock lyrics not found exception")
        }

        if (trackName == "throw invalidJson") {
            throw InvalidJsonException("Mock invalid json exception")
        }

        if (trackName == "throw exception") {
            throw Exception("Mock generic exception")
        }

        return "lyrics"
    }
}