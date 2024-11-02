package cc.wordview.wordfind

import cc.wordview.wordfind.mock.ConvertedVTT
import cc.wordview.wordfind.mock.LRC
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class LrcToVttTest {
    private val LRC = LRC()
    private val convertedVTT = ConvertedVTT()

    @Test
    fun convert() {
        testConversion(LRC.shuuten, convertedVTT.shuuten)
        testConversion(LRC.helloWorld, convertedVTT.helloWorld)
        testConversion(LRC.suiseiNoParade, convertedVTT.suiseiNoParade)
    }

    @Test
    fun convertIndexOutOfBounds() {
        assertDoesNotThrow {
            val converted = LrcToVtt().convert(LRC.indexOutOfBounds)
            assertFalse { converted.isEmpty() }
        }
    }

    private fun testConversion(lrc: String, expectedVtt: String) {
        val converted = LrcToVtt().convert(lrc)
        assertEquals(expectedVtt, converted)
    }
}