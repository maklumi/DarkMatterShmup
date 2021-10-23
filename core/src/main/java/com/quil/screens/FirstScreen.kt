package com.quil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.quil.DarkMatterMain
import ktx.log.logger

private val LOG = logger<Firstcreen>()

class Firstcreen(game: DarkMatterMain) : DarkMatterScreen(game) {

    override fun show() {
        super.show()
        LOG.debug { "First screen" }
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            game.setScreen<SecondScreen>()
        }
    }

}