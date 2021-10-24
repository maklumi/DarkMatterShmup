package com.quil


import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.ecs.system.PlayerAnimationSystem
import com.quil.ecs.system.PlayerInputSystem
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

    //    private val defaultRegion by lazy { TextureRegion(Texture(Gdx.files.internal("graphics/ship_base.png"))) }
//    private val leftRegion by lazy { TextureRegion(Texture(Gdx.files.internal("graphics/ship_left.png"))) }
//    private val rightRegion by lazy { TextureRegion(Texture(Gdx.files.internal("graphics/ship_right.png"))) }
    private val gameAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/graphics.atlas")) }

    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(
                PlayerAnimationSystem(
//                    defaultRegion, leftRegion, rightRegion
                    gameAtlas.findRegion("ship_base"),
                    gameAtlas.findRegion("ship_left"),
                    gameAtlas.findRegion("ship_right")
                )
            )
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
        gameAtlas.dispose()
    }
}