package Scenes

import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {
        solidRect(10,10,color = Colors.GREEN).xy(0,0)
    }
}