package com.quil.screens

import com.badlogic.gdx.graphics.g2d.Batch
import com.quil.DarkMatterMain
import com.quil.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {

    override fun show() {
        engine.entity {
            with<TransformComponent> {
                position.set(4.5f, 8f, 0f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

}