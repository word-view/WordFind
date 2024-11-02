package cc.wordview.wordfind.providers

import cc.wordview.wordfind.exception.LyricsNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue
import kotlin.test.Ignore

class LrcLibTest {
    val mockWISHResponse = "{\"id\":3490559,\"name\":\"WISH\",\"trackName\":\"WISH\",\"artistName\":\"majiko\",\"albumName\":\"寂しい人が一番偉いんだ\",\"duration\":294.0,\"instrumental\":false,\"plainLyrics\":\"もし一つ願いが叶うなら\nきっと僕は\n君の夢が叶うようにって\n願い事するんだろうな\n\n世界がなくなっても\n君がいてくれるなら\n怖くはないから\n暗闇でも手を繋いで\n\nI wonder why\n涙が出るんだ\n戻らない過去はあるけど\nただ君と二人で\n歩こう泥濘んだ道も\n願い事の先はここにある\n\n戻らない過去が邪魔をして\n立ち止まったなら\n僕が新しい思い出をあげる\n泣かないでいいんだよ\n\n高い壁があっても\n二人なら越えられる\nほら 笑ってみせて\n未来へと手を繋いで\n\nI can see how\n逃げたくないんだ\n苦しい明日が来ても\n二人で歩いた道は\nたくさんの花が咲くって\n信じてる\n僕のそばにいて\n\nI wonder why\n涙が出るけど\n悲しい涙じゃないんだ\nただ君と二人で\n進もう泥濘んだ道も\n願い事の先はここにある\",\"syncedLyrics\":\"[00:25.51] もし一つ願いが叶うなら\n[00:30.45] きっと僕は\n[00:36.70] 君の夢が叶うようにって\n[00:41.28] 願い事するんだろうな\n[00:47.56] 世界がなくなっても\n[00:53.48] 君がいてくれるなら\n[00:58.94] 怖くはないから\n[01:04.38] 暗闇でも手を繋いで\n[01:11.32] I wonder why\n[01:15.55] 涙が出るんだ\n[01:17.86] 戻らない過去はあるけど\n[01:24.11] ただ君と二人で\n[01:29.05] 歩こう泥濘んだ道も\n[01:35.05] 願い事の先はここにある\n[01:47.18] \n[02:00.53] 戻らない過去が邪魔をして\n[02:05.48] 立ち止まったなら\n[02:11.64] 僕が新しい思い出をあげる\n[02:19.59] 泣かないでいいんだよ\n[02:22.72] 高い壁があっても\n[02:28.14] 二人なら越えられる\n[02:33.98] ほら 笑ってみせて\n[02:39.45] 未来へと手を繋いで\n[02:46.33] I can see how\n[02:50.43] 逃げたくないんだ\n[02:53.08] 苦しい明日が来ても\n[02:59.15] 二人で歩いた道は\n[03:03.95] たくさんの花が咲くって\n[03:10.27] 信じてる\n[03:15.62] 僕のそばにいて\n[03:21.52] \n[03:50.41] I wonder why\n[03:54.41] 涙が出るけど\n[03:57.14] 悲しい涙じゃないんだ\n[04:03.16] ただ君と二人で\n[04:07.92] 進もう泥濘んだ道も\n[04:14.12] 願い事の先はここにある\n[04:26.04] \"}"
    val mockWISHResponseNotSynced = "{\"id\":3490559,\"name\":\"WISH\",\"trackName\":\"WISH\",\"artistName\":\"majiko\",\"albumName\":\"寂しい人が一番偉いんだ\",\"duration\":294.0,\"instrumental\":false,\"plainLyrics\":\"もし一つ願いが叶うなら\nきっと僕は\n君の夢が叶うようにって\n願い事するんだろうな\n\n世界がなくなっても\n君がいてくれるなら\n怖くはないから\n暗闇でも手を繋いで\n\nI wonder why\n涙が出るんだ\n戻らない過去はあるけど\nただ君と二人で\n歩こう泥濘んだ道も\n願い事の先はここにある\n\n戻らない過去が邪魔をして\n立ち止まったなら\n僕が新しい思い出をあげる\n泣かないでいいんだよ\n\n高い壁があっても\n二人なら越えられる\nほら 笑ってみせて\n未来へと手を繋いで\n\nI can see how\n逃げたくないんだ\n苦しい明日が来ても\n二人で歩いた道は\nたくさんの花が咲くって\n信じてる\n僕のそばにいて\n\nI wonder why\n涙が出るけど\n悲しい涙じゃないんだ\nただ君と二人で\n進もう泥濘んだ道も\n願い事の先はここにある\"}"

    @Test
    @Ignore("This test makes a request to the real API, only run it if something inside the find method changed that was not related to parseResponse")
    fun realSearch() {
        assertDoesNotThrow {
            val response = LrcLib().find("WISH", "majiko")

            assertTrue(response.isNotEmpty())
            assertTrue(response.isNotBlank())
        }
    }

    @Test
    fun simpleSearch() {
        assertDoesNotThrow {
            val response = LrcLib().parseResponse(mockWISHResponse, 200)
            assertTrue(response.isNotEmpty())
            assertTrue(response.isNotEmpty())
        }
    }

    @Test
    fun notFound() {
        assertThrows<LyricsNotFoundException> {
            LrcLib().parseResponse(mockWISHResponse, 404)
        }
    }

    @Test
    fun foundButNotSynced() {
        assertThrows<LyricsNotFoundException> {
            LrcLib().parseResponse(mockWISHResponseNotSynced, 200)
        }
    }

    @Test
    fun abnormalResponseStatus() {
        assertThrows<Exception> {
            LrcLib().parseResponse(mockWISHResponse, 418)
        }
    }
}