package com.quil.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.quil.DarkMatterMain
import ktx.app.KtxScreen

abstract class DarkMatterScreen(
    val game: DarkMatterMain,
    val batch: Batch = game.batch,
    val engine: Engine = game.engine
) : KtxScreen