package com.quil.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.quil.asset.BitmapFontAsset
import com.quil.asset.TextureAtlasAsset
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

enum class LabelStyles {
    DEFAULT, GRADIENT,
}

fun createSkin(assets: AssetStorage) {
    val atlas = assets[TextureAtlasAsset.UI.descriptor]
    val gradientFont = assets[BitmapFontAsset.FONT_LARGE_GRADIENT.descriptor]
    val normalFont = assets[BitmapFontAsset.FONT_DEFAULT.descriptor]
    Scene2DSkin.defaultSkin = skin(atlas) { skin: Skin ->
        label(LabelStyles.DEFAULT.name) {
            font = normalFont
        }
        label(LabelStyles.GRADIENT.name) {
            font = gradientFont
        }
    }
}