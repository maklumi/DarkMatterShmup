package com.quil


import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.quil.screens.Firstcreen
import com.quil.screens.SecondScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class DarkMatterMain : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(Firstcreen(this))
        addScreen(SecondScreen(this))
        setScreen<Firstcreen>()
    }
}