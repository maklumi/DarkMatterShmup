package com.quil

import com.badlogic.gdx.Game
import com.quil.FirstScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class DarkMatterMain : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}