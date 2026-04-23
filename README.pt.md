# WordFind

[![](https://jitpack.io/v/word-view/WordFind.svg)](https://jitpack.io/#word-view/WordFind)

WordFind é uma simples biblioteca que procura por letras de músicas temporizadas em diversas plataformas na internet.
A biblioteca procura através de todas as plataformas até achar a letra correta, adicionalmente o usuário da biblioteca 
pode adicionar uma nova plataforma se quiser através da implementação da abstract class `Provider`.

# Plataformas suportadas no momento
- Musixmatch
- LRCLIB
- NetEase

# Modo de uso
Para a instalação siga o setup do jitpack: https://jitpack.io/#word-view/WordFind

```kotlin

    val wordfind = WordFind()
    
    wordfind.find(trackName = "Eleanor Rigby", artistName = "The Beatles", convertToVtt = true)
    
    // Omita o parâmetro convertToVtt se quiser a letra em formato LRC
    wordfind.find(trackName = "Eleanor Rigby", artistName = "The Beatles")

```