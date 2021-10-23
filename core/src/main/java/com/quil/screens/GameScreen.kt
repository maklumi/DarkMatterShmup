package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.quil.DarkMatterMain
import com.quil.UNIT
import com.quil.ecs.component.GraphicComponent
import com.quil.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {
    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_base.png"))

    override fun show() {
        repeat(10) {
            engine.entity {
                with<TransformComponent> {
                    position.set(MathUtils.random(0f, 9f), MathUtils.random(0f, 16f), 0f)
                }
                with<GraphicComponent>() {
                    sprite.run {
                        setRegion(playerTexture)
                        setSize(texture.width * UNIT, texture.height * UNIT)
                        setOriginCenter() // center is used for rotation
                    }
                }
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

}