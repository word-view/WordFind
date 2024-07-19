package cc.wordview.wordfind;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Lrc2VttTest {
        @Test
        void convertHelloWorld() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockValues.helloWorldLrc);
                        assertFalse(converted.isEmpty());
                });
        }

        @Test
        void convertPossibleIndexOutOfBounds() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockValues.indexOutOfBoundsLrc);
                        assertFalse(converted.isEmpty());
                });
        }

        @Test
        void convertSuiseiNoParade() {
                assertDoesNotThrow(() -> {
                        StringBuffer converted = Lrc2Vtt.convert(MockValues.suiseiNoParadeLrc);
                        assertFalse(converted.isEmpty());
                });
        }
}
