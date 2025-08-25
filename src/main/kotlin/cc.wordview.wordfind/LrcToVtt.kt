package cc.wordview.wordfind

/**
 * Converts the LRC format into VTT. It's easier to work with VTT in android and other situations.
 */
class LrcToVtt {
    fun convert(lrcString: String): String {
        val buffer = StringBuilder()

        val lines = lrcString.split("\n")

        val timings = ArrayList<String>()
        val cues = ArrayList<String>()

        for (line in lines) {
            if (line.isEmpty()) continue

            val startBracket = line.indexOf('[')
            val endBracket = line.indexOf(']')

            if (startBracket == -1 || endBracket == -1 || endBracket <= startBracket) continue

            val time = line.substring(startBracket + 1, endBracket)
            val cue = line.substring(endBracket + 1).trim()

            timings.add(time)
            cues.add(cue)
        }

        // Start mounting the VTT
        buffer.append("WEBVTT\n\n")
        buffer.append("# This WEBVTT was converted from LRC and might contain errors\n\n")

        for (i in 0.. timings.size - 2) {
            var currentTiming = timings[i]
            var nextTiming = timings[i + 1]

            if (currentTiming.split(Regex("\\."))[1].length == 2) {
                currentTiming += "0"
            }

            if (nextTiming.split(Regex("\\."))[1].length == 2) {
                nextTiming += "0"
            }

            // This code assumes the song is no longer than 1 hour, which probably won't be.
            buffer.append("00:$currentTiming --> 00:$nextTiming\n")
            buffer.append("${cues[i]}\n\n")
        }

        return buffer.toString()
    }
}