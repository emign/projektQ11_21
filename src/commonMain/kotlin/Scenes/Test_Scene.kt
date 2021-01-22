package Scenes

//import fsm.Entity
import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import fsm.MakeOwnEntityTemplate

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val test = MakeOwnEntityTemplate.build("spineboy-pro.skel", "spineboy-pma.atlas", 1.0f)

        container {
            speed = 1.0
            scale(1.0)
            position(400, 800)
            addChild(test.modelView)
            //solidRect(10.0, 10.0, Colors.RED).centered
            addUpdater {
                if (views.keys.justPressed(Key.J)) test.stateManager.doStateChange(test.jumpState)
                if (views.keys.justPressed(Key.D)) test.stateManager.doStateChange(test.deathState)
            }
        }

        solidRect(200, 1080).xy(1900, 0)

    }
}