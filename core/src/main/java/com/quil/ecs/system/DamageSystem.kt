package com.quil.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.quil.ecs.component.PlayerComponent
import com.quil.ecs.component.RemoveComponent
import com.quil.ecs.component.TransformComponent
import com.quil.event.GameEvent
import com.quil.event.GameEventManager
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get
import kotlin.math.max

const val DAMAGE_AREA_HEIGHT = 2f
private const val DAMAGE_PER_SECOND = 25f
private const val DEATH_EXPLOSION_DURATION = 0.9f

class DamageSystem(
    private val gameEventManager: GameEventManager
) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class.java).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        requireNotNull(transform) { "Entity |entity| must have a TransformComponent. entity=$entity" }
        val player = entity[PlayerComponent.mapper]
        requireNotNull(player) { "Entity |entity| must have a PlayerComponent. entity=$entity" }

        if (transform.position.y <= DAMAGE_AREA_HEIGHT) {
            var damage = DAMAGE_PER_SECOND * deltaTime
            if (player.shield > 0f) {
                val blockAmount = player.shield
                player.shield = max(0f, player.shield - damage)
                damage -= blockAmount

                if (damage <= 0f) {
                    return
                }
            }

            player.life -= damage
            gameEventManager.dispatchEvent(GameEvent.PlayerHit.apply {
                this.player = entity
                this.life = player.life
                this.maxLife = player.maxLife
            })

            if (player.life <= 0f) {
                gameEventManager.dispatchEvent(
                    GameEvent.PlayerDeath.apply {
                        this.distance = player.distance
                    })
                entity.addComponent<RemoveComponent>(engine) {
                    delay = DEATH_EXPLOSION_DURATION
                }
            }
        }
    }
}