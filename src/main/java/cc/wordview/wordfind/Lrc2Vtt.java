package cc.wordview.wordfind;

import java.util.ArrayList;

public class Lrc2Vtt {
        public static StringBuffer convert(String lrc) {
                StringBuffer buffer = new StringBuffer();

                String[] split = lrc.split("\n");

                ArrayList<String> timings = new ArrayList<>();
                ArrayList<String> cues = new ArrayList<>();

                for (String line : split) {
                        var time = line.split("[\\[\\]]")[1];
                        var cueSplit = line.replaceFirst("]", "%DELIMITER%").split("%DELIMITER%");

                        if (cueSplit.length < 2) continue;

                        var cue = cueSplit[1].trim();

                        timings.add(time);
                        cues.add(cue);
                }

                buffer.append("WEBVTT\n\n");
                buffer.append("# This WEBVTT was converted from a LRC format\n\n");

                for (int i = 0; i < timings.size() - 1; i++) {
                        // this assumes a song is never an hour long which most likely it won't be anyway
                        buffer.append("00:%s0 --> 00:%s0%n".formatted(timings.get(i), timings.get(i + 1)));
                        buffer.append("%s%n%n".formatted(cues.get(i)));
                }

                return buffer;
        }
}
