package Scenes

//import fsm.Entity
import DragonbonesEntityTemplate
import character.CharacterBase
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

        addComponent(Input(this, bus))
        repeat(50) {

            val testEntity = DragonbonesEntityTemplate.build(
                "armatureName",
                "DragonBones/Demon_ske.json",
                "DragonBones/Demon_tex.json",
                "DragonBones/Demon_tex.png",
                bus
            )

            //val testSpine = MakeOwnEntityTemplate.build("spineboy-pro.skel", "spineboy-pma.atlas", 1.0f)

            container {
                speed = 1.0
                scale(1.0)
                position(400, 800)
                addChild(testEntity.model)
            }

            bus.register<IdleEvent> { testEntity.stateManager.doStateChange(testEntity.idleState) }
            bus.register<RightEvent> { testEntity.stateManager.doStateChange(testEntity.runState) }
            bus.register<JumpEvent> { testEntity.stateManager.doStateChange(testEntity.jumpState) }

        }
    }
}