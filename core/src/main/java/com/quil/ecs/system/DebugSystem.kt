package com.quil.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.quil.ecs.component.PlayerComponent
import com.quil.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.getSystem
import ktx.log.logger
import kotlin.math.max
import kotlin.math.min

private const val WINDOW_INFO_UPDATE_RATE = 0.25f
private val LOG = logger<DebugSystem>()

class DebugSystem : IntervalIteratingSystem(allOf(PlayerComponent::class).get(), WINDOW_INFO_UPDATE_RATE) {
    init {
        setProcessing(true)
    }

    override fun processEntity(entity: Entity) {
        val transform = entity[TransformComponent.mapper]
        requireNotNull(transform) { "Entity |entity| must have a TransformComponent. entity=$entity" }
        val player = entity[PlayerComponent.mapper]
        requireNotNull(player) { "Entity |entity| must have a PlayerComponent. entity=$entity" }

        when {
            Gdx.input.isKeyPressed(Input.Keys.P) -> {
                // kill player
                transform.position.y = 1f
                player.life = 1f
                player.shield = 0f
            }
            Gdx.input.isKeyPressed(Input.Keys.O) -> {
                // add shield
                player.shield = max(player.shield, player.shield + 25f)
            }
            Gdx.input.isKeyPressed(Input.Keys.I) -> {
                // remove shield
                player.shield = max(0f, player.shield - 25f)

            }
            Gdx.input.isKeyPressed(Input.Keys.U) -> {
                // disable movement
                engine.getSystem<MoveSystem>().setProcessing(false)
            }
            Gdx.input.isKeyPressed(Input.Keys.Y) -> {
                // disable movement
                engine.getSystem<MoveSystem>().setProcessing(true)
            }
        }

        Gdx.graphics.setTitle("pos:${transform.position}, life:${player.life}, shield:${player.shield}")
    }
}