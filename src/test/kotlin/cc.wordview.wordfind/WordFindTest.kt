package cc.wordview.wordfind

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class WordFindTest {
    @Test
    fun search() {
        testSearch("majiko suisei no parade")
        testSearch("do fundo da grota")
        testSearch("majiko 彗星のパレード", LyricsProviders.NETEASE)
        testSearch("wonderland", LyricsProviders.NETEASE)
    }

    @Test
    fun searchLyricsNotFound() {
        assertThrows<LyricsNotFoundException> {
            WordFind().search("majiko_suisei_no_parade")
        }
    }

    @Test
    fun invalidExecutable() {
        assertThrows<IOException> {
            val client = WordFind()

            client.executablePath = "aaaaaaaaaaaaaa"
            client.search("a")
        }
    }

    private fun testSearch(query: String) {
        val result = WordFind().search(query)

        assertNotNull(result)
        assertFalse(result.isEmpty())
        assertFalse(result.isBlank())
    }

    private fun testSearch(query: String, provider: LyricsProviders) {
        val result = WordFind().search(query, provider)

        assertNotNull(result)
        assertFalse(result.isEmpty())
        assertFalse(result.isBlank())
    }
}