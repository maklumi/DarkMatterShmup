package com.quil.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.quil.ecs.component.RemoveComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RemoveSystem : IteratingSystem(allOf(RemoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComponent = entity[RemoveComponent.mapper]
        requireNotNull(removeComponent) { "Entity |entity| must have a RemoveComponent. entity=$entity" }

        removeComponent.delay -= deltaTime
        if (removeComponent.delay <= 0f) {
            engine.removeEntity(entity)
        }
    }
}