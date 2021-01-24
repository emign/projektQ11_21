package Scenes

//import fsm.Entity
import com.soywiz.korge.dragonbones.KorgeDbFactory
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
        val factory = KorgeDbFactory()
        addComponent(Input(this, bus))


        repeat(1) {

            val testEntity = DragonbonesEntityTemplate.build(
                "Armature",
                "Dragonbones_Test/HandTest_ske.json",
                "Dragonbones_Test/HandTest_tex.json",
                "Dragonbones_Test/HandTest_tex.png",
                factory
            )

            container {
                speed = 1.0
                scale(0.6)
                position(500, 500)
                addChild(testEntity.model)
                //solidRect(10.0, 10.0, Colors.RED).centered
            }
            bus.register<IdleEvent> { testEntity.stateManager.doStateChange(testEntity.idleState) }
            bus.register<RightEvent> { testEntity.stateManager.doStateChange(testEntity.runState) }
            bus.register<JumpEvent> { testEntity.stateManager.doStateChange(testEntity.jumpState) }
        }
        solidRect(200, 1080).xy(1900, 0)
    }
}