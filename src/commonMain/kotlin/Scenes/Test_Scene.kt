package Scenes

import fsm.Entity
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {
        repeat(50) {
            val e = Entity()
            e.view.xy((0..1400).random(), (0..800).random())
            addChild(e.view)
        }

    }
}