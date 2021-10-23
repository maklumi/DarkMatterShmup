package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.DarkMatterMain
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {
    private val viewport = FitViewport(9f, 16f) // set our viewport to 9 units x 16 units
    private val texture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val sprite = Sprite(texture).apply {
        setSize(1f, 1f) // set to 1 unit dimension otherwise the original is 9 by 10 units
    }

    override fun show() {
        LOG.debug { "First screen" }
        sprite.setPosition(1f, 1f)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply() // if using multiple viewports
        batch.use(viewport.camera.combined) {
            sprite.draw(it)
        }
    }

    override fun dispose() {
        super.dispose()
        texture.dispose()
    }
}