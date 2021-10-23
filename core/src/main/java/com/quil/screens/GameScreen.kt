package com.quil.screens

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.quil.DarkMatterMain
import com.quil.ecs.component.FacingComponent
import com.quil.ecs.component.GraphicComponent
import com.quil.ecs.component.PlayerComponent
import com.quil.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {

    override fun show() {
        engine.entity {
            with<TransformComponent> {
                position.set(MathUtils.random(0f, 9f), MathUtils.random(0f, 16f), 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

}