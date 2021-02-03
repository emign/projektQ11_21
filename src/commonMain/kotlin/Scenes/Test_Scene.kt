package Scenes

//import fsm.Entity
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import eventBus.*
import eventBus.Input
import fsm.*
import multiplayer.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {
        val m = Multiplayer("192.168.1.113",9400,true,scope = this@TestScene)
        m.setCallback { println(it) }
        keys.down { m.send(test()) }
    }
}

class test : MultiplayerData{
    override fun toString(): String {
        return "hello"
    }

    override fun fromString(str: String) {

    }

}