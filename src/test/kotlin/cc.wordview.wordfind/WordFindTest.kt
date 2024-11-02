package cc.wordview.wordfind

import cc.wordview.wordfind.exception.InvalidJsonException
import cc.wordview.wordfind.exception.LyricsNotFoundException
import cc.wordview.wordfind.mock.MockProvider
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class WordFindTest {
    @Test
    @Ignore("This test makes a request to the real API, only run it if something inside the find method changed that was not related to parseResponse")
    fun realFind() {
        assertDoesNotThrow {
            // This search will result from NetEase, the last platform in the order specified
            // at the WordFind class, that ensures that when a platform fails it goes to the next
            val lyrics  = WordFind().find("彗星のパレード", "まじ娘", convertToVtt = true)

            assertTrue(lyrics.isNotEmpty())
            assertTrue(lyrics.isNotBlank())
        }
    }

    @Test
    fun find() {
        assertDoesNotThrow { 
            val lyrics  = WordFind().find("", "", MockProvider())
            assertEquals("lyrics", lyrics)
        }
    }

    @Test
    fun findLyricsNotFound() {
        assertThrows<LyricsNotFoundException> {
            WordFind().find("throw notFound", "", MockProvider())
        }
    }

    @Test
    fun findInvalidJson() {
        assertThrows<InvalidJsonException> {
            WordFind().find("throw invalidJson", "", MockProvider())
        }
    }

    @Test
    fun findGenericException() {
        assertThrows<Exception> {
            WordFind().find("throw exception", "", MockProvider())
        }
    }
}