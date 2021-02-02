package Scenes

//import fsm.Entity
import physic.Listener
import physic.*
import actor.Enemy
import actor.Player
import actor.actors.Bullet
import com.soywiz.klock.DateTime
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Point
import eventBus.*
import fsm.*
import org.jbox2d.common.Vec2

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        //val bus = EventBus(this@TestScene)

        /*val testPlayer = Player.build("Characters/Test.xml", this@TestScene).apply { xy(100, 100); scale = 0.1 }
        val testEnemy = Enemy.build("Characters/Test.xml", this@TestScene).apply { xy(300, 300); scale = 0.1 }
        val testPlayer2 = Player.build("Characters/Test.xml", this@TestScene).apply { xy(500, 500); scale = 0.1 }
        val testBullet = Bullet.build("Characters/Test.xml", this@TestScene).apply { xy(700, 700); scale = 0.1 }
*/

        val s1 = SolidRect(100, 100, Colors.RED).xy(50, 50).apply { name = "Rot" }
        val s2 = SolidRect(200, 800, Colors.BLUE).xy(400, 400).apply { name = "Blau" }
        val s3 = SolidRect(50, 200, Colors.GREEN).xy(300, 500).apply { name = "Grün" }
        val s4 = SolidRect(75, 120, Colors.YELLOW).xy(100, 500).apply { name = "Gelb" }
        val s5 = SolidRect(75, 120, Colors.PURPLE).xy(100, 300).apply { name = "Purple" }

        val listener = Listener(gravityAcc = Vec2(0.0f, 9.81f))

        val p1 = Physics(
            s1, listener, Physics.PhysicsInfo(
                s1.width, s1.height, Vec2(s1.x.toFloat(), s1.y.toFloat()), Vec2(0.0f, 0.0f),
                Vec2(2.0f, 0.5f), true, 120.0, 120.0
            ) { me, other -> println("Ich heisse ${me.owner.name} und kollidiere mit ${other.owner.name}") }
        )

        val p2 = Physics(
            s2, listener, Physics.PhysicsInfo(
                s2.width, s2.height, Vec2(s2.x.toFloat(), s2.y.toFloat()), Vec2(0.0f, 0.0f),
                Vec2(2.0f, 0.5f), false, 0.01, 0.01
            ) { _, _ -> }
        )

        val p3 = Physics(
            s3, listener, Physics.PhysicsInfo(
                s3.width, s3.height, Vec2(s3.x.toFloat(), s3.y.toFloat()), Vec2(0.0f, 0.0f),
                Vec2(2.0f, 0.5f), false, 0.01, 0.01
            ) { _, _ -> }
        )

        val p4 = Physics(
            s4, listener, Physics.PhysicsInfo(
                s4.width, s4.height, Vec2(s4.x.toFloat(), s4.y.toFloat()), Vec2(0.0f, 0.0f),
                Vec2(2.0f, 0.5f), false, 0.01, 0.01
            ) { _, _ -> }
        )

        val p5 = Physics(
            s5, listener, Physics.PhysicsInfo(
                s5.width, s5.height, Vec2(s5.x.toFloat(), s5.y.toFloat()), Vec2(0.0f, 0.0f),
                Vec2(2.0f, 0.5f), true, 120.0, 120.0
            ) { me, other -> println("Ich heisse ${me.owner.name} und kollidiere mit ${other.owner.name}. Aber ich bin der Zweite") }
        )

        addChild(s1)
        addChild(s2)
        addChild(s3)
        addChild(s4)
        addChild(s5)

        listener.addPhysics(p1)
        listener.addPhysics(p2)
        listener.addPhysics(p3)
        listener.addPhysics(p4)
        listener.addPhysics(p5)

        addUpdater {
            if (views.keys[Key.UP]) if (p1.isGrounded) p1.addForce(Vec2(0.0f, -400.0f))
            if (views.keys[Key.LEFT]) p1.addForce(Vec2(-10.0f, 0.0f))
            if (views.keys[Key.RIGHT]) p1.addForce(Vec2(10.0f, 0.0f))
            if (views.keys[Key.W]) if (p5.isGrounded) p5.addForce(Vec2(0.0f, -400.0f))
            if (views.keys[Key.A]) p5.addForce(Vec2(-10.0f, 0.0f))
            if (views.keys[Key.D]) p5.addForce(Vec2(10.0f, 0.0f))
            listener.update(it.milliseconds.toFloat())
            /*if (views.keys.justPressed(Key.RIGHT)) testPlayer.bus.send(StateTransition(testPlayer.walkState))
            if (views.keys.justPressed(Key.SPACE)) testPlayer.bus.send(StateTransition(testPlayer.jumpState))
            if (views.keys.justReleased(Key.RIGHT)) testPlayer.bus.send(StateTransition(testPlayer.idleState))*/
        }
    }
}