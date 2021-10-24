package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.quil.DarkMatterMain
import com.quil.ecs.component.FacingComponent
import com.quil.ecs.component.GraphicComponent
import com.quil.ecs.component.PlayerComponent
import com.quil.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatterMain, batch: Batch) : DarkMatterScreen(game, batch) {

    private val character = engine.entity {
        with<TransformComponent> {
            position.set(2f, 2f, 0f)
        }
        with<GraphicComponent> {
            setSpriteRegion(game.gameAtlas.findRegion("character"))
        }
    }

    override fun show() {
        engine.entity {
            with<TransformComponent> {
                position.set(4.5f, 8f, 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
        engine.entity {
            with<TransformComponent> {
                position.set(1f, 1f, 0f)
            }
            with<GraphicComponent> {
                setSpriteRegion(game.gameAtlas.findRegion("ship_left"))
            }
        }
        engine.entity {
            with<TransformComponent> {
                position.set(8f, 1f, 0f)
            }
            with<GraphicComponent> {
                setSpriteRegion(game.gameAtlas.findRegion("ship_right"))
            }
        }
        engine.entity {
            with<TransformComponent> {
                position.set(3f, 1.5f, 0f)
                size.set(1f, 2f)
            }
            with<GraphicComponent> {
                setSpriteRegion(game.gameAtlas.findRegion("tree"))
            }
        }
    }

    override fun render(delta: Float) {
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> character[TransformComponent.mapper]!!.position.x -= 0.25f
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> character[TransformComponent.mapper]!!.position.x += 0.25f
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> character[TransformComponent.mapper]!!.position.y += 0.25f
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> character[TransformComponent.mapper]!!.position.y -= 0.25f
        }
        engine.update(delta)
    }

}