package com.quil.screens

import com.quil.DarkMatterMain
import com.quil.UNIT_SCALE
import com.quil.V_WIDTH
import com.quil.ecs.component.*
import com.quil.ecs.system.DAMAGE_AREA_HEIGHT
import ktx.ashley.entity
import ktx.ashley.with
import kotlin.math.min

private const val MAX_DELTA_TIME = 1 / 20f

class GameScreen(game: DarkMatterMain) : DarkMatterScreen(game) {

    override fun show() {
        val playerShip = engine.entity {
            with<TransformComponent> {
                setInitialPosition(4.5f, 8f, -1f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
        engine.entity {
            with<TransformComponent>()
            with<AttachComponent> {
                entity = playerShip
                offset.set(1f * UNIT_SCALE, -6f * UNIT_SCALE)
            }
            with<GraphicComponent>()
            with<AnimationComponent> { type = AnimationType.FIRE }
        }
        engine.entity {
            with<TransformComponent> {
                size.set(V_WIDTH.toFloat(), DAMAGE_AREA_HEIGHT)
            }
            with<AnimationComponent> { type = AnimationType.DARK_MATTER }
            with<GraphicComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

}