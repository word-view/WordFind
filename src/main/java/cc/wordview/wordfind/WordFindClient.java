package cc.wordview.wordfind;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WordFindClient {
        @Getter
        @Setter
        private static String executablePath = "syncedlyrics";

        @Getter
        @Setter
        private static LyricsProvider defaultProvider = LyricsProvider.MUSIXMATCH;

        public static String search(String query) throws IOException, LyricsNotFoundException {
                return search(query, defaultProvider);
        }

        public static String search(String query, LyricsProvider provider) throws IOException, LyricsNotFoundException {
                Process process;

                ProcessBuilder builder = getProcessBuilder(query, provider);

                builder.directory(new File(System.getProperty("java.io.tmpdir")));

                process = builder.start();

                String stdout = inputStreamToString(process.getInputStream());
                String stderr = inputStreamToString(process.getErrorStream());

                if (!stderr.isEmpty())
                        throw new IOException("Failed to run syncedlyrics: %s".formatted(stderr));

                if (stdout.isEmpty())
                        throw new LyricsNotFoundException("Unable to find any lyrics for this search query in %s".formatted(provider.getName()));

                return stdout;
        }

        private static ProcessBuilder getProcessBuilder(String query, LyricsProvider provider) {
                ProcessBuilder builder;

                if (provider == LyricsProvider.ALL) {
                        builder = new ProcessBuilder(
                                executablePath,
                                "--synced-only",
                                "--output=/dev/null", // ignore output file
                                "\"%s\"".formatted(query)
                        );
                } else {
                        builder = new ProcessBuilder(
                                executablePath,
                                "--synced-only",
                                "-p=%s".formatted(provider.getName()),
                                "--output=/dev/null",
                                "\"%s\"".formatted(query)
                        );
                }
                return builder;
        }

        private static String inputStreamToString(InputStream stream) throws IOException {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
}