package cc.wordview.wordfind

private data class LyricEntry(val time: String, val cue: String)

/**
 * Converts the LRC format into VTT. It's easier to work with VTT in android and other situations.
 */
class LrcToVtt {
    fun convert(lrcString: String): String {
        val buffer = StringBuilder()

        val lines = lrcString.split("\n")

        val entries = ArrayList<LyricEntry>()

        for (line in lines) {
            if (line.isEmpty()) continue
            val startBracket = line.indexOf('[')
            val endBracket = line.indexOf(']')
            if (startBracket == -1 || endBracket == -1 || endBracket <= startBracket) continue
            val time = line.substring(startBracket + 1, endBracket)
            val cue = line.substring(endBracket + 1).trim()
            entries.add(LyricEntry(time, cue))
        }

        // Start mounting the VTT
        buffer.append("WEBVTT\n\n")
        buffer.append("# This WEBVTT was converted from LRC and might contain errors\n\n")

        for (i in 0 until entries.size - 1) {
            val currentEntry = entries[i]
            val nextEntry = entries[i+1]
            val currentTime = padTimestamp(currentEntry.time)
            val nextTime = padTimestamp(nextEntry.time)
            // In here we are assuming the song is no longer than 1 hour
            buffer.append("00:$currentTime --> 00:$nextTime\n")
            buffer.append("${currentEntry.cue}\n\n")
        }

        return buffer.toString()
    }

    private fun padTimestamp(time: String): String {
        val parts = time.split(".")
        if (parts.size != 2) return time
        val milliseconds = parts[1]
        return if (milliseconds.length == 2) "${time}0" else time
    }
}