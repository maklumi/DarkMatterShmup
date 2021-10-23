package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.quil.DarkMatterMain
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {
    private val texture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val sprite = Sprite(texture)

    override fun show() {
        super.show()
        LOG.debug { "First screen" }
        sprite.setPosition(1f, 1f)
    }

    override fun render(delta: Float) {
        batch.use {
            sprite.draw(it)
        }
    }

    override fun dispose() {
        super.dispose()
        texture.dispose()
    }
}