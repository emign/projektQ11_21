import com.soywiz.klock.DateTime
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import org.jbox2d.common.Vec2
import physic.Listener
import physic.Physics
import kotlin.reflect.KFunction

suspend fun main() = Korge {
    val s1 = SolidRect(100, 100, Colors.RED).xy(50, 50).apply { name = "Rot" }.apply {

    }
    val s2 = SolidRect(200, 800, Colors.BLUE).xy(400, 400).apply { name = "Blau" }.apply {

    }
    val s3 = SolidRect(50, 200, Colors.GREEN).xy(300, 500).apply { name = "Grün" }.apply {

    }
    val s4 = SolidRect(75, 120, Colors.YELLOW).xy(100, 500).apply { name = "Gelb" }.apply {

    }

    val listener = Listener(Vec2(0.0f, 9.81f))

    val p1 = Physics(s1, listener, Physics.PhysicsInfo(s1.width, s1.height, Vec2(s1.x.toFloat(), s1.y.toFloat()), Vec2(0.0f, 0.0f),
        Vec2(2.0f, 0.5f), true, 120.0, 120.0))
    val p2 = Physics(s2, listener, Physics.PhysicsInfo(s2.width, s2.height, Vec2(s2.x.toFloat(), s2.y.toFloat()), Vec2(0.0f, 0.0f),
        Vec2(2.0f, 0.5f), false, 0.01, 0.01))
    val p3 = Physics(s3, listener, Physics.PhysicsInfo(s3.width, s3.height, Vec2(s3.x.toFloat(), s3.y.toFloat()), Vec2(0.0f, 0.0f),
        Vec2(2.0f, 0.5f), false, 0.01, 0.01))
    val p4 = Physics(s4, listener, Physics.PhysicsInfo(s4.width, s4.height,  Vec2(s4.x.toFloat(), s4.y.toFloat()), Vec2(0.0f, 0.0f),
        Vec2(2.0f, 0.5f), false, 0.01, 0.01))

    addChild(s1)
    addChild(s2)
    addChild(s3)
    addChild(s4)

    listener.addPhysics(p1)
    listener.addPhysics(p2)
    listener.addPhysics(p3)
    listener.addPhysics(p4)

    addUpdater {
        println(it.milliseconds)
        //if (views.keys[Key.DOWN]) {}
        if (views.keys[Key.UP]) if (p1.isGrounded) p1.addForce(Vec2(0.0f, -700.0f))
        if (views.keys[Key.LEFT]) p1.addForce(Vec2(-10.0f, 0.0f))
        if (views.keys[Key.RIGHT]) p1.addForce(Vec2(10.0f, 0.0f))
        listener.update(it.milliseconds.toFloat())
        /*if (views.keys.justPressed(Key.RIGHT)) testPlayer.bus.send(StateTransition(testPlayer.walkState))
        if (views.keys.justPressed(Key.SPACE)) testPlayer.bus.send(StateTransition(testPlayer.jumpState))
        if (views.keys.justReleased(Key.RIGHT)) testPlayer.bus.send(StateTransition(testPlayer.idleState))*/
    }
}
