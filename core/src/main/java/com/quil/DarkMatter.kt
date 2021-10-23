package com.quil


import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.ecs.system.RenderSystem
import com.quil.screens.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val LOG = logger<DarkMatterMain>()
const val UNIT = 1 / 16f // 1 world unit to 16 pixels

class DarkMatterMain : KtxGame<KtxScreen>() {
    val gameViewport = FitViewport(9f, 16f)
    val batch by lazy { SpriteBatch() }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(RenderSystem(batch, gameViewport))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen(this, batch))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
    }
}