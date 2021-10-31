package com.quil.screens

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.quil.DarkMatterMain
import com.quil.audio.AudioService
import com.quil.event.GameEventManager
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

abstract class DarkMatterScreen(
    val game: DarkMatterMain,
    val gameViewport: Viewport = game.gameViewport,
    val uiViewport: Viewport = game.uiViewport,
    val gameEventManager: GameEventManager = game.gameEventManager,
    val assets: AssetStorage = game.assets,
    val audioService: AudioService = game.audioService,
    val preferences: Preferences = game.preferences,
    val stage: Stage = game.stage,
) : KtxScreen {
    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }
}