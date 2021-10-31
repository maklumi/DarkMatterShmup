package com.quil.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram

enum class TextureAsset(
    filename: String,
    directory: String = "graphics",
    val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$directory/$filename", Texture::class.java)
) {
    BACKGROUND("background.png"),

}

enum class TextureAtlasAsset(
    filename: String,
    directory: String = "graphics",
    val isSkinAtlas: Boolean = false,
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$filename", TextureAtlas::class.java)
) {
    GAME_GRAPHICS("graphics.atlas"),
    UI("ui.atlas", directory = "ui", isSkinAtlas = true),

}

enum class SoundAsset(
    filename: String,
    directory: String = "sound",
    val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$directory/$filename", Sound::class.java)
) {
    BOOST_1("boost1.wav"),
    BOOST_2("boost2.wav"),
    LIFE("life.wav"),
    SHIELD("shield.wav"),
    DAMAGE("damage.wav"),
    BLOCK("block.wav"),
}

enum class MusicAsset(
    filename: String,
    directory: String = "music",
    val descriptor: AssetDescriptor<Music> = AssetDescriptor("$directory/$filename", Music::class.java)
) {
    GAME("game.mp3")
}

enum class ShaderProgramAsset(
    vertexFilename: String,
    fragmentFilename: String,
    directory: String = "shader",
    val descriptor: AssetDescriptor<ShaderProgram> = AssetDescriptor(
        "$directory/$vertexFilename/$fragmentFilename",
        ShaderProgram::class.java,
        ShaderProgramLoader.ShaderProgramParameter().apply {
            vertexFile = "$directory/$vertexFilename"
            fragmentFile = "$directory/$fragmentFilename"
        }
    )
) {
    OUTLINE("default.vert", "outline.frag"),
}

enum class BitmapFontAsset(
    filename: String,
    directory: String = "ui",
    val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor(
        "$directory/$filename",
        BitmapFont::class.java,
        BitmapFontLoader.BitmapFontParameter().apply {
            atlasName = TextureAtlasAsset.UI.descriptor.fileName
        }
    )
) {
    FONT_LARGE_GRADIENT("font11_gradient.fnt"),
    FONT_DEFAULT("font8.fnt"),
}