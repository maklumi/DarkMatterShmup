package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.quil.DarkMatterMain
import com.quil.asset.ShaderProgramAsset
import com.quil.asset.SoundAsset
import com.quil.asset.TextureAsset
import com.quil.asset.TextureAtlasAsset
import com.quil.ui.LabelStyles
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger
import ktx.scene2d.*

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: DarkMatterMain) : DarkMatterScreen(game) {
    private lateinit var progressBar: Image
    private lateinit var touchToBeginLabel: Label

    override fun show() {
        val timeMillis = System.currentTimeMillis()
        // queue asset loading
        val assetRefs = gdxArrayOf(
            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
            TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
            ShaderProgramAsset.values().map { assets.loadAsync(it.descriptor) },
        ).flatten()

        // then change to game screen
        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Time for loading assets: ${System.currentTimeMillis() - timeMillis} ms" }
            assetsLoaded()
        }

        setupUI()
    }

    private fun setupUI() {
        stage.actors {
            table {
                defaults().fillX().expandX()

                label("Loading Screen", LabelStyles.GRADIENT.name) {
                    setWrap(true)
                    setAlignment(Align.center)
                }
                row()

                touchToBeginLabel = label("Touch to begin", LabelStyles.DEFAULT.name) {
                    setWrap(true)
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()

                stack { cell ->
                    progressBar = image("life_bar").apply {
                        scaleX = 0f
                    }
                    label("Loading...", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }

                setFillParent(true)
                pack()
            }
        }
        stage.setDebugAll(false)
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        touchToBeginLabel += Actions.forever(
            Actions.sequence(
                Actions.fadeIn(0.5f) + Actions.fadeOut(0.5f)
            )
        )
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<GameScreen>()) {
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
        progressBar.scaleX = assets.progress.percent
        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

}