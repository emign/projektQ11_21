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

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val bus = EventBus(this@TestScene)

        addComponent(Input(this,bus))

        val background = Image(resourcesVfs["spineboy-pma.png"].readBitmap()).apply {
            //xy(0,0)

            onClick {
                println("geklickt")
            }
        }

        addChild(background)



        repeat(50) {

            val testEntity = MakeOwnEntityTemplate.build("spineboy-pro.skel", "spineboy-pma.atlas", 1.0f)

            container {
                speed = 1.0
                scale(0.5)
                position(400, 800)
                addChild(testEntity.modelView)
                //solidRect(10.0, 10.0, Colors.RED).centered
            }
            bus.register<IdleEvent>{testEntity.stateManager.doStateChange(testEntity.idleState)}
            bus.register<RightEvent>{testEntity.stateManager.doStateChange(testEntity.runState)}
            bus.register<JumpEvent>{testEntity.stateManager.doStateChange(testEntity.jumpState)}
        }
        solidRect(200, 1080).xy(1900, 0)
    }
}