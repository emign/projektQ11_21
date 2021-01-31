package Scenes

//import fsm.Entity
import physic.Listener
import physic.*
import actor.Enemy
import actor.Player
import actor.actors.Bullet
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import eventBus.*
import fsm.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        //val bus = EventBus(this@TestScene)

        /*val testPlayer = Player.build("Characters/Test.xml", this@TestScene)
        val testEnemy = Enemy.build("Characters/Test.xml", this@TestScene)
        val testPlayer2 = Player.build("Characters/Test.xml", this@TestScene)
        val testBullet = Bullet.build("Characters/Test.xml", this@TestScene)
*/

        val s1 = SolidRect(100, 100, Colors.RED).xy(50, 50)
        val s2 = SolidRect(200, 800, Colors.BLUE).xy(400, 400)
        val s3 = SolidRect(50, 200, Colors.GREEN).xy(300, 500)
        val s4 = SolidRect(75, 120, Colors.YELLOW).xy(100, 500)

        val a1 = AABB(s1)
        val a2 = AABB(s2)
        val a3 = AABB(s3)
        val a4 = AABB(s4)

        addChild(s1)
        addChild(s2)
        addChild(s3)
        addChild(s4)

        val list = mutableListOf<AABB>(a1, a2, a3, a4)

        val listener = Listener(list)

        addUpdater {
            val collidables = listener.update()
            collidables.forEach {
                println("${it.r1.owner.color.toString()} kollidiert mit ${it.r2.owner.color.toString()}")
            }
            if (views.keys[Key.DOWN]) s1.y += 3
            if (views.keys[Key.UP]) s1.y -= 3
            if (views.keys[Key.LEFT]) s1.x -= 3
            if (views.keys[Key.RIGHT]) s1.x += 3
            /*if (views.keys.justPressed(Key.RIGHT)) testPlayer.bus.send(StateTransition(testPlayer.walkState))
            if (views.keys.justPressed(Key.SPACE)) testPlayer.bus.send(StateTransition(testPlayer.jumpState))
            if (views.keys.justReleased(Key.RIGHT)) testPlayer.bus.send(StateTransition(testPlayer.idleState))*/
        }
    }
}