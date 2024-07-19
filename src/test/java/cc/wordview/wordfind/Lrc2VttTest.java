package cc.wordview.wordfind;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Lrc2VttTest {
        @Test
        void convertShuutenNoSaki() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockLrc.shuutenNoSakiLrc);
                        assertEquals(ConvertedVtt.shuutenNoSakiVtt, converted.toString());
                });
        }

        @Test
        void convertHelloWorld() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockLrc.helloWorldLrc);
                        assertEquals(ConvertedVtt.helloWorldVtt, converted.toString());
                });
        }

        @Test
        void convertPossibleIndexOutOfBounds() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockLrc.indexOutOfBoundsLrc);
                        assertFalse(converted.isEmpty());
                });
        }

        @Test
        void convertSuiseiNoParade() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockLrc.suiseiNoParadeLrc);
                        assertEquals(ConvertedVtt.suiseiNoParadeVtt, converted.toString());
                });
        }
}
