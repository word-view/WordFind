package cc.wordview.wordfind.extensions

import cc.wordview.wordfind.exception.InvalidJsonException
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlin.jvm.Throws

/**
 * A wrapper around `JsonObject.get()` that throws a `InvalidJsonException` if the member is null.
 *
 * @param memberName name of the member being requested
 * @return the value of the member
 */
@Throws(InvalidJsonException::class)
fun JsonObject.getOrThrow(memberName: String): JsonElement {
    val member = this.get(memberName)

    if (member == null || member.isJsonNull)
        throw InvalidJsonException("Field '$memberName' is null")
    else return member
}