[Portuguese](https://github.com/word-view/WordFind/blob/master/README.pt.md) | [日本語](https://github.com/word-view/WordFind/blob/master/README.ja.md)

# WordFind

[![](https://jitpack.io/v/word-view/WordFind.svg)](https://jitpack.io/#word-view/WordFind)

WordFind is a simple library to search for time synced lyrics through online platforms. 
The library loops through all the platforms until it finds the appropriate lyrics, additionally a new platform can be
added by the consumer of the library by implementing the `Provider` abstract class.

# Currently supported platforms
- Musixmatch
- LRCLIB
- NetEase

# Usage
For the installation, set up the jitpack repository following the jitpack's how to: https://jitpack.io/#word-view/WordFind

```kotlin

    val wordfind = WordFind()
    
    wordfind.find(trackName = "Eleanor Rigby", artistName = "The Beatles", convertToVtt = true)
    
    // Omit convertToVtt to get an LRC version.
    wordfind.find(trackName = "Eleanor Rigby", artistName = "The Beatles")

```