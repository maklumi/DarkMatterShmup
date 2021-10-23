package com.quil.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.quil.ecs.component.FacingComponent
import com.quil.ecs.component.FacingDirection
import com.quil.ecs.component.GraphicComponent
import com.quil.ecs.component.PlayerComponent
import ktx.ashley.allOf
import ktx.ashley.get

class PlayerAnimationSystem(
    private val defaultRegion: TextureRegion,
    private val leftRegion: TextureRegion,
    private val rightRegion: TextureRegion
) : IteratingSystem(allOf(PlayerComponent::class, FacingComponent::class, GraphicComponent::class).get()),
    EntityListener {
    private var lastDirection = FacingDirection.DEFAULT

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[GraphicComponent.mapper]?.setSpriteRegion(defaultRegion)
    }

    override fun entityRemoved(entity: Entity) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        requireNotNull(facing) { "Entity |entity| must have a FacingComponent. entity=$entity" }
        val graphic = entity[GraphicComponent.mapper]
        requireNotNull(graphic) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (facing.direction == lastDirection && graphic.sprite.texture != null) {
            // no change in direction and texture already set -> do nothing
            return
        }

        lastDirection = facing.direction
        val region = when (facing.direction) {
            FacingDirection.LEFT -> leftRegion
            FacingDirection.DEFAULT -> defaultRegion
            FacingDirection.RIGHT -> rightRegion
        }
        graphic.setSpriteRegion(region)
    }
}