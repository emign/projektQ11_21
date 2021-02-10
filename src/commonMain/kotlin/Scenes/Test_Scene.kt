package Scenes

//import fsm.Entity
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import eventBus.*
import kotlinx.coroutines.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        /*val testPlayer = Player.build("Characters/Test.xml", this@TestScene).apply { xy(100, 100); scale = 0.1 }
        val testEnemy = Enemy.build("Characters/Test.xml", this@TestScene).apply { xy(300, 300); scale = 0.1 }
        val testPlayer2 = Player.build("Characters/Test.xml", this@TestScene).apply { xy(500, 500); scale = 0.1 }
        val testBullet = Bullet.build("Characters/Test.xml", this@TestScene).apply { xy(700, 700); scale = 0.1 }
*/

    }
}