package com.quil


import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.ecs.system.*
import com.quil.screens.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val LOG = logger<DarkMatterMain>()
const val UNIT_SCALE = 1 / 16f // 1 world unit to 16 pixels
const val V_WIDTH = 9
const val V_HEIGHT = 16

class DarkMatterMain : KtxGame<KtxScreen>() {
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch by lazy { SpriteBatch() }

    val gameAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/graphics.atlas")) }

    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(DamageSystem())
            addSystem(
                PlayerAnimationSystem(
                    gameAtlas.findRegion("ship_base"),
                    gameAtlas.findRegion("ship_left"),
                    gameAtlas.findRegion("ship_right")
                )
            )
            addSystem(AttachSystem())
            addSystem(AnimationSystem(gameAtlas))
            addSystem(RenderSystem(batch, gameViewport))
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        gameAtlas.dispose()
    }
}