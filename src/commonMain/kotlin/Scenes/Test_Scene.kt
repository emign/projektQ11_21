package Scenes

//import fsm.Entity
import com.soywiz.korev.GameButton
import com.soywiz.korev.Key
import com.soywiz.korge.ext.swf.SWFExportConfig
import com.soywiz.korge.ext.swf.readSWF
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.vector.ShapeRasterizerMethod
import com.soywiz.korio.file.std.resourcesVfs
import fsm.MakeOwnEntityTemplate

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val background = Image(resourcesVfs["spineboy-pma.png"].readBitmap()).apply {
            //xy(0,0)

            onClick {
                println("geklickt")
            }
        }

        addChild(background)



        repeat(50) {

            this += resourcesVfs["dog.swf"].readSWF(views).createMainTimeLine().apply {
                xy((0..1000).random(), (0..800).random())
            }

            val testEntity = MakeOwnEntityTemplate.build("spineboy-pro.skel", "spineboy-pma.atlas", 1.0f)

            container {
                speed = 1.0
                scale(0.5)
                position(400, 800)
                addChild(testEntity.modelView)
                //solidRect(10.0, 10.0, Colors.RED).centered
                addUpdater {
                    if (views.keys.justPressed(Key.J)) testEntity.stateManager.doStateChange(testEntity.jumpState)
                    if (views.keys.justPressed(Key.D)) testEntity.stateManager.doStateChange(testEntity.deathState)
                    if (views.keys.justPressed(Key.RIGHT)) testEntity.stateManager.doStateChange(testEntity.runState)
                    if (views.keys.justReleased(Key.RIGHT)) testEntity.stateManager.doStateChange(testEntity.idleState)
                }
            }

            solidRect(200, 1080).xy(1900, 0)

        }
    }
}