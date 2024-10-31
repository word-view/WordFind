package cc.wordview.wordfind

import java.io.IOException
import java.io.InputStream

class WordFind {
    var executablePath = "syncedlyrics"

    var defaultProvider = LyricsProviders.MUSIXMATCH

    fun search(query: String): String {
        return search(query, defaultProvider)
    }

    fun search(query: String, provider: LyricsProviders): String {
        val builder = getProcessBuilder(query, provider)
        val process = builder.start()

        val stdout = inputStreamToString(process.inputStream)
        val stderr = inputStreamToString(process.errorStream)

        if (stderr.isNotEmpty()) throw IOException("Failed to run syncedlyrics: $stderr")

        if (stdout.isEmpty()) throw LyricsNotFoundException("Could not find anything for query=$query")

        return stdout
    }

    private fun getProcessBuilder(query: String, provider: LyricsProviders): ProcessBuilder {
        return ProcessBuilder(
            executablePath,
            "--synced-only",
            "-p=${provider.platformName}",
            "--output=/dev/null",
            query
        )
    }

    private fun inputStreamToString(stream: InputStream): String {
        return stream.bufferedReader().use { it.readText() }
    }
}