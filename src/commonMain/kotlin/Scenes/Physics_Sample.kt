package Scenes

import physic.addPhysicsComponent
import physic.addPhysicsComponentsTo
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import org.jbox2d.common.Vec2
import physic.physics
import physic.setupPhysicsSystem

class Physics_Sample : Scene() {
    override suspend fun Container.sceneInit() {

        /**
         * Everything needed to work with physic.getPhysics-objects is contained in [PhysicsModule.kt].
         * All the other things are managed internal
         */

        //create 5 SolidRects
        val s1 = Circle(radius = 50.0, Colors.RED).xy(50, 50).apply { name = "Rot" }
        val s2 = SolidRect(200, 800, Colors.BLUE).xy(400, 400).apply { name = "Blau" }
        val s3 = SolidRect(50, 200, Colors.GREEN).xy(300, 500).apply { name = "Grün" }
        val s4 = SolidRect(75, 120, Colors.YELLOW).xy(100, 500).apply { name = "Gelb" }
        val s5 = Circle(75.0, Colors.PURPLE).xy(100, 300).apply { name = "Purple" }

        //add them to the stage
        addChild(s1)
        addChild(s2)
        addChild(s3)
        addChild(s4)
        addChild(s5)

        //create a physic.getPhysics listener -> this function has to be called before attaching physicComponents to views, otherwise the physic.getPhysics will not be updated
        setupPhysicsSystem()


        //add physic.getPhysics to s1 and s5 and attach a collisionCallback
        s1.addPhysicsComponent(friction = Vec2(2.0f, 0.5f), isDynamic = true, layer = 4, coefficient = Vec2(120.0f, 120.0f)) { other ->
            println("Hey, ich kollidiere gerade mit $other")
        }

        s5.addPhysicsComponent(layer = 1) {
            println("Ich ${owner.name} kollidiere mit ${it.owner.name}")
        }

        //add physic.getPhysics to s2, s3 and s4, but they should be solid (not dynamic) and have no callback
        addPhysicsComponentsTo(s2, s3, s4, isDynamic = false, layer = 3)
        //s4.addPhysicsComponent(isDynamic = false, layer = 4, collisionCallback = {/* Nothing */})



        //simple input system for moving s1 and s5
        addUpdater {
            keys.down {  }
            if (views.keys[Key.UP]) if (s1.physics?.isGrounded == true) s1.physics?.addForce(Vec2(0.0f, -400.0f))
            if (views.keys[Key.LEFT]) s1.physics?.addForce(Vec2(-10.0f, 0.0f))
            if (views.keys[Key.RIGHT]) s1.physics?.addForce(Vec2(10.0f, 0.0f))
            //if (views.keys[Key.W]) if (s5.physics?.isGrounded == true) s5.physics?.addForce(Vec2(0.0f, -400.0f))
            //if (views.keys[Key.A]) s5.physics?.addForce(Vec2(-10.0f, 0.0f))
            //if (views.keys[Key.D]) s5.physics?.addForce(Vec2(10.0f, 0.0f))
        }
    }
}