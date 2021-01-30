package Scenes

//import fsm.Entity
import actor.CharacterBase
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import eventBus.*
import fsm.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val bus = EventBus(this@TestScene)

        val test = CharacterBase.build("Characters/Test.xml", this@TestScene)
        addChild(test).apply { xy(500, 500) }
        addUpdater {
            if (views.keys.justPressed(Key.RIGHT)) test.bus.send(StateTransition(test.walkState))
            if (views.keys.justPressed(Key.SPACE)) test.bus.send(StateTransition(test.getDamageState))
            if (views.keys.justReleased(Key.RIGHT)) test.bus.send(StateTransition(test.idleState))
        }
    }
}