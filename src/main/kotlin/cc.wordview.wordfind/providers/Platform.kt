package cc.wordview.wordfind.providers

/**
 * List of all the default supported platforms.
 *
 * @property platformName A friendly name of the platform.
 * @property provider The provider implementation
 */
enum class Platform(val platformName: String, val provider: Provider) {
    LRCLIB("LRCLIB", LrcLib()),
    NETEASE("NetEase", NetEase()),
    MUSIXMATCH("MusixMatch", MusixMatch())
}