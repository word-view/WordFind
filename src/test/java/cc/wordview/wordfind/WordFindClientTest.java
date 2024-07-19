package cc.wordview.wordfind;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WordFindClientTest {
        @Test
        @Order(1)
        void search() {
                assertDoesNotThrow(() -> {
                        String result = WordFindClient.search("majiko suisei no parade");

                        assertNotNull(result);
                        assertFalse(result.isEmpty());
                        assertFalse(result.isBlank());
                });
        }

        @Test
        @Order(2)
        void searchNetEase() {
                assertDoesNotThrow(() -> {
                        String result = WordFindClient.search("majiko 彗星のパレード", LyricsProvider.NETEASE);

                        assertNotNull(result);
                        assertFalse(result.isEmpty());
                        assertFalse(result.isBlank());
                });
                assertDoesNotThrow(() -> {
                        String result = WordFindClient.search("majiko magic", LyricsProvider.NETEASE);

                        assertNotNull(result);
                        assertFalse(result.isEmpty());
                        assertFalse(result.isBlank());
                });
        }

        @Test
        @Order(3)
        void searchGauches() {
                assertDoesNotThrow(() -> {
                        String result = WordFindClient.search("do fundo da grota");

                        assertNotNull(result);
                        assertFalse(result.isEmpty());
                        assertFalse(result.isBlank());
                });
        }

        @Test
        @Order(4)
        void searchLyricsNotFound() {
                assertThrows(LyricsNotFoundException.class, () -> {
                        WordFindClient.search("majiko_suisei_no_parade");
                });
        }

        @Test
        @Order(5)
        void searchInvalidExecutable() {
                assertThrows(IOException.class, () -> {
                        WordFindClient.setExecutablePath("syncliri");
                        WordFindClient.search("bora bill");
                });
        }
}
