package com.quil


import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.quil.screens.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class DarkMatterMain : KtxGame<KtxScreen>() {
    private val batch by lazy { SpriteBatch() }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen(this, batch))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
    }
}