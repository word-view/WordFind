package cc.wordview.wordfind;

import lombok.Getter;

@Getter
public enum LyricsProvider {
        MUSIXMATCH("musixmatch"),
        NETEASE("netease"),
        LRCLIB("lrclib"),
        MEGALOBIZ("megalobiz"),
        GENIUS("genius");

        private final String name;

        LyricsProvider(String name) {
                this.name = name;
        }
}
