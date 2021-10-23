package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.quil.DarkMatterMain
import com.quil.UNIT
import com.quil.ecs.component.GraphicComponent
import com.quil.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {
    private val viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val player = engine.entity {
        with<TransformComponent>() {
            position.set(1f, 1f, 0f)
        }
        with<GraphicComponent>() {
            sprite.run {
                setRegion(playerTexture)
                setSize(texture.width * UNIT, texture.height * UNIT)
                setOriginCenter() // center is used for rotation
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        engine.update(delta)

        viewport.apply()
//        batch.use(viewport.camera.combined) {
//            player[GraphicComponent.mapper]?.sprite?.draw(it)
//        }
        batch.use(viewport.camera.combined) { batch ->
            player[GraphicComponent.mapper]?.let { graphic ->
                player[TransformComponent.mapper]?.let { transform ->
                    graphic.sprite.run {
                        rotation = transform.rotationDeg
                        setBounds(transform.position.x, transform.position.y, transform.size.x, transform.size.y)
                        draw(batch)
                    }
                }
            }

        }
    }

}