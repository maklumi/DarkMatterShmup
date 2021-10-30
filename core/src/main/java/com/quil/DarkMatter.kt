package com.quil


import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.asset.TextureAsset
import com.quil.asset.TextureAtlasAsset
import com.quil.audio.AudioService
import com.quil.audio.DefaultAudioService
import com.quil.ecs.system.*
import com.quil.event.GameEventManager
import com.quil.screens.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.logger

private val LOG = logger<DarkMatterMain>()
const val UNIT_SCALE = 1 / 16f // 1 world unit to 16 pixels
const val V_WIDTH = 9
const val V_HEIGHT = 16
const val V_WIDTH_PIXELS = 135
const val V_HEIGHT_PIXELS = 240

class DarkMatterMain : KtxGame<KtxScreen>() {
    val uiViewport = FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat())
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch by lazy { SpriteBatch() }
    val gameEventManager = GameEventManager()
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }
    val audioService: AudioService by lazy { DefaultAudioService(assets) }

    val engine by lazy {
        PooledEngine().apply {
            val gameAtlas = assets[TextureAtlasAsset.GAME_GRAPHICS.descriptor]
            val backgroundTexture = assets[TextureAsset.BACKGROUND.descriptor]

            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PowerUpSystem(gameEventManager, audioService))
            addSystem(DamageSystem(gameEventManager))
            addSystem(CameraShakeSystem(gameViewport.camera, gameEventManager))
            addSystem(
                PlayerAnimationSystem(
                    gameAtlas.findRegion("ship_base"),
                    gameAtlas.findRegion("ship_left"),
                    gameAtlas.findRegion("ship_right")
                )
            )
            addSystem(AttachSystem())
            addSystem(AnimationSystem(gameAtlas))
            addSystem(RenderSystem(batch, gameViewport, uiViewport, backgroundTexture, gameEventManager))
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        assets.dispose()
    }
}