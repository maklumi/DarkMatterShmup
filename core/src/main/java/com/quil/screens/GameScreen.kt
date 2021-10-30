package com.quil.screens

import com.badlogic.ashley.core.Engine
import com.quil.DarkMatterMain
import com.quil.UNIT_SCALE
import com.quil.V_WIDTH
import com.quil.asset.MusicAsset
import com.quil.ecs.component.*
import com.quil.ecs.system.DAMAGE_AREA_HEIGHT
import com.quil.event.GameEvent
import com.quil.event.GameEventListener
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import kotlin.math.min

private const val MAX_DELTA_TIME = 1 / 20f
private val LOG = logger<GameScreen>()

class GameScreen(
    game: DarkMatterMain,
    private val engine: Engine = game.engine
) : DarkMatterScreen(game), GameEventListener {

    override fun show() {
        LOG.debug { "High score: ${preferences["highscore", 0f]}" }
        gameEventManager.addListener(GameEvent.PlayerDeath::class, this)

        audioService.play(MusicAsset.GAME)
        spawnPlayer()

        engine.entity {
            with<TransformComponent> {
                size.set(V_WIDTH.toFloat(), DAMAGE_AREA_HEIGHT)
            }
            with<AnimationComponent> { type = AnimationType.DARK_MATTER }
            with<GraphicComponent>()
        }
    }

    private fun spawnPlayer() {
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
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
        audioService.update()
    }

    override fun hide() {
        super.hide()
        gameEventManager.removeListener(this)
    }

    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.PlayerDeath -> {
                LOG.debug { "Player reaches a distance of ${event.distance} " }
                preferences.flush {
                    this["highscore"] = event.distance
                }
                spawnPlayer()
            }
            GameEvent.CollectPowerUp -> {
            }
        }
    }
}