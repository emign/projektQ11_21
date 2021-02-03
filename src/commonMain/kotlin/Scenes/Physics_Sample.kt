package Scenes

import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import org.jbox2d.common.Vec2
import physic.addPhysicsComponentsTo
import physic.physics
import physic.usePhysics

class Physics_Sample : Scene() {
    override suspend fun Container.sceneInit() {

        //create 5 SolidRects
        val s1 = SolidRect(100, 100, Colors.RED).xy(50, 50).apply { name = "Rot" }
        val s2 = SolidRect(200, 800, Colors.BLUE).xy(400, 400).apply { name = "Blau" }
        val s3 = SolidRect(50, 200, Colors.GREEN).xy(300, 500).apply { name = "Grün" }
        val s4 = SolidRect(75, 120, Colors.YELLOW).xy(100, 500).apply { name = "Gelb" }
        val s5 = SolidRect(75, 120, Colors.PURPLE).xy(100, 300).apply { name = "Purple" }

        //add them to the stage
        addChild(s1)
        addChild(s2)
        addChild(s3)
        addChild(s4)
        addChild(s5)

        //create a physics listener -> this function has to be called before attaching physicComponents to views, otherwise the physics will not be updated
        usePhysics()

        //add physics to s1 and s5 and attach a collisionCallback
        addPhysicsComponentsTo(s1, s5) {
            println("Ich ${owner.name} kollidiere mit ${it.owner.name}")
        }

        //add physics to s2, s3 and s4, but they should be solid (not dynamic) and have no callback
        addPhysicsComponentsTo(s2, s3, s4, isDynamic = false)

        //simple input system for moving s1 and s5
        addUpdater {
            if (views.keys[Key.UP]) if (s1.physics?.isGrounded == true) s1.physics?.addForce(Vec2(0.0f, -400.0f))
            if (views.keys[Key.LEFT]) s1.physics?.addForce(Vec2(-10.0f, 0.0f))
            if (views.keys[Key.RIGHT]) s1.physics?.addForce(Vec2(10.0f, 0.0f))
            if (views.keys[Key.W]) if (s5.physics?.isGrounded == true) s5.physics?.addForce(Vec2(0.0f, -400.0f))
            if (views.keys[Key.A]) s5.physics?.addForce(Vec2(-10.0f, 0.0f))
            if (views.keys[Key.D]) s5.physics?.addForce(Vec2(10.0f, 0.0f))
        }
    }
}