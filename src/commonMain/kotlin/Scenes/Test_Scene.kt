package Scenes

//import fsm.Entity
import com.esotericsoftware.spine.korge.skeletonView
import com.soywiz.korev.Key
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import fsm.daTestEnemy

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val test = daTestEnemy.build("spineboy-pro.skel", "spineboy-pma.atlas", 1.0f)

        container {
            speed = 1.0
            scale(1.0)
            position(400, 800)
            addChild(test.modelView)
            //solidRect(10.0, 10.0, Colors.RED).centered
            addUpdater {
                if(views.keys.justPressed(Key.J)) test.stateSystem.doStateChange(test.jumpState)
            }
        }

        solidRect(200, 1080).xy(1900, 0)

    }
}