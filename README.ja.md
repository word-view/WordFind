# WordFind

[![](https://jitpack.io/v/word-view/WordFind.svg)](https://jitpack.io/#word-view/WordFind)

WordFindは基本的に簡単な時間を持っている音楽の歌詞をオンラインで探すライブラリーです。
このライブラリーは全部のプラットフォームを一つ一つを試して、そして一番良いを選んでしている、それよりライブラリーのユーザーは自分で
新たなプラットフォームを注入しているのは可能です、方法は新しいなクラスを作ってそして`Provider`のクラスをエクステンドしているだけです。

# 現在に入るプラットフォーム達
- Musixmatch
- LRCLIB
- NetEase

# ご利用
インストールのはjitpackのチュートリアルを見てください： https://jitpack.io/#word-view/WordFind

```kotlin

    val wordfind = WordFind()
    
    wordfind.find(trackName = "Eleanor Rigby", artistName = "The Beatles", convertToVtt = true)
    
    // LRCが欲しいならconvertToVttを抜きして下さい
    wordfind.find(trackName = "Eleanor Rigby", artistName = "The Beatles")

```
