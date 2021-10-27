package com.quil.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

private const val DEFAULT_FRAME_DURATION = 1 / 20f

enum class AnimationType(
    val atlasKey: String,
    val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
    val speedRate: Float = 1f
) {
    NONE(""),
    DARK_MATTER("dark_matter", Animation.PlayMode.LOOP, 1f),
    FIRE("fire"),
    SPEED_1("orb_blue", speedRate = 0.5f), SPEED_2("orb_yellow"),
    LIFE("life"), SHIELD("shield", speedRate = 0.75f),
}

class Animation2D(
    val type: AnimationType,
    keyframes: Array<out TextureRegion>,
    playMode: PlayMode = PlayMode.LOOP,
    speedRate: Float = 1f
) : Animation<TextureRegion>(DEFAULT_FRAME_DURATION / speedRate, keyframes, playMode)

class AnimationComponent : Component, Pool.Poolable {
    var type = AnimationType.NONE
    var stateTime = 0f
    lateinit var animation: Animation2D

    override fun reset() {
        type = AnimationType.NONE
        stateTime = 0f
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}