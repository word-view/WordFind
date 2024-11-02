package cc.wordview.wordfind.exception

/**
 * Thrown when the library fails to find a property inside the json response.
 */
class InvalidJsonException(override val message: String) : Exception()