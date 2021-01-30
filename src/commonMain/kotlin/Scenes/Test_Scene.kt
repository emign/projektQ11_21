package Scenes

//import fsm.Entity
import character.CharacterBase
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import eventBus.*
import eventBus.Input
import fsm.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val bus = EventBus(this@TestScene)

        val test = CharacterBase("Characters/Test.xml", bus, this@TestScene)
        if (views.keys.justPressed(Key.RIGHT)) {
            test.doStateChange(test.walkState)
            test.x += 5
        }
        if (views.keys.justPressed(Key.LEFT)) {
            test.doStateChange(test.walkState)
            test.x -= 5
        }
        if (!views.keys[Key.LEFT] && !views.keys[Key.RIGHT]) {
            test.doStateChange(test.idleState)
        }
    }
}