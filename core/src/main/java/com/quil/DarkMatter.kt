package com.quil


import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.asset.*
import com.quil.audio.AudioService
import com.quil.audio.DefaultAudioService
import com.quil.ecs.system.*
import com.quil.event.GameEventManager
import com.quil.screens.LoadingScreen
import com.quil.ui.createSkin
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger

// https://www.youtube.com/watch?v=25_xCStxi9g&list=PLTKHCDn5RKK-8lZmjZoG4rFywN_SLbZR8&index=26
private val LOG = logger<DarkMatterMain>()
const val UNIT_SCALE = 1 / 16f // 1 world unit to 16 pixels
const val V_WIDTH = 9
const val V_HEIGHT = 16
const val V_WIDTH_PIXELS = 135
const val V_HEIGHT_PIXELS = 240

class DarkMatterMain : KtxGame<KtxScreen>() {
    val uiViewport = FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat())
    val stage: Stage by lazy {
        val result = Stage(uiViewport, batch)
        Gdx.input.inputProcessor = result
        result
    }
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch by lazy { SpriteBatch() }
    val gameEventManager = GameEventManager()
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }
    val audioService: AudioService by lazy { DefaultAudioService(assets) }
    val preferences: Preferences by lazy { Gdx.app.getPreferences("preferences-game-dark-matter") }

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
            addSystem(
                RenderSystem(
                    batch,
                    gameViewport,
                    uiViewport,
                    backgroundTexture,
                    gameEventManager,
                    assets[ShaderProgramAsset.OUTLINE.descriptor]
                )
            )
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        val assetRefs = gdxArrayOf(
            TextureAtlasAsset.values().filter { it.isSkinAtlas }.map { assets.loadAsync(it.descriptor) },
            BitmapFontAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            createSkin(assets)
            addScreen(LoadingScreen(this@DarkMatterMain))
            setScreen<LoadingScreen>()
        }
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Number of sprites in batch: ${batch.maxSpritesInBatch}" }
        MusicAsset.values().forEach {
            LOG.debug { "Refcount $it: ${assets.getReferenceCount(it.descriptor)}" }
        }
        batch.dispose()
        assets.dispose()
        stage.dispose()
    }
}