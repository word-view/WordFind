package cc.wordview.wordfind.extensions

import kotlin.math.max
import kotlin.math.min

// original (https://github.com/aallam/string-similarity-kotlin/blob/main/string-similarity/src/commonMain/kotlin/com/aallam/similarity/NormalizedLevenshtein.kt)

/**
 * Compute the similarity between two string.
 * Corresponds to 1.0 - normalized distance.
 *
 * @param rhs right hand side string to compare.
 * @return the computer similarity
 */
fun String.similarity(rhs: CharSequence): Double {
    return 1.0 - distance(this.lowercase(), rhs)
}


private fun distance(first: CharSequence, second: CharSequence): Double {
    val maxLength = max(first.length, second.length)
    if (maxLength == 0) return 0.0
    return lDistance(first, second) / maxLength.toDouble()
}

private fun lDistance(first: CharSequence, second: CharSequence, limit: Int = Int.MAX_VALUE): Int {
    if (first == second) return 0
    if (first.isEmpty()) return second.length
    if (second.isEmpty()) return first.length

    // initial costs is the edit distance from an empty string, which corresponds to the characters to inserts.
    // the array size is : length + 1 (empty string)
    var cost = IntArray(first.length + 1) { it }
    var newCost = IntArray(first.length + 1)

    for (i in 1..second.length) {

        // calculate new costs from the previous row.
        // the first element of the new row is the edit distance (deletes) to match empty string
        newCost[0] = i

        var minCost = i

        // fill in the rest of the row
        for (j in 1..first.length) {
            // if it's the same char at the same position, no edit cost.
            val edit = if (first[j - 1] == second[i - 1]) 0 else 1
            val replace = cost[j - 1] + edit
            val insert = cost[j] + 1
            val delete = newCost[j - 1] + 1
            newCost[j] = minOf(insert, delete, replace)
            minCost = min(minCost, newCost[j])
        }

        if (minCost >= limit) return limit

        // flip references of current and previous row
        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost.last()
}
