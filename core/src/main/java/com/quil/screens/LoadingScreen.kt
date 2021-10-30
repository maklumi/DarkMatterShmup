package com.quil.screens

import com.quil.DarkMatterMain
import com.quil.asset.ShaderProgramAsset
import com.quil.asset.SoundAsset
import com.quil.asset.TextureAsset
import com.quil.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: DarkMatterMain) : DarkMatterScreen(game) {
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

    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}