package Scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import org.jbox2d.common.Vec2
import physic.*
import physic.internal.PhysicsDirection
import physic.internal.times

class Physics_Sample : Scene() {
    override suspend fun Container.sceneInit() {

        /**
         * Everything needed to work with physic.getPhysics-objects is contained in [PhysicsComponent.kt].
         * All the other things are managed internal
         */

        //create some SolidRects
        val s2 = SolidRect(200, 800, Colors.BLUE).xy(400, 400).apply { name = "Blau" }
        val s3 = SolidRect(50, 200, Colors.GREEN).xy(300, 500).apply { name = "Grün" }
        val s4 = SolidRect(75, 120, Colors.YELLOW).xy(100, 500).apply { name = "Gelb" }
        val s5 = SolidRect(55.0, 100.0, Colors.PURPLE).xy(100, 300).apply { name = "Purple" }

        //add them to the stage
        addChild(s2)
        addChild(s3)
        addChild(s4)
        addChild(s5)

        //create a physic.getPhysics listener -> this function has to be called before attaching physicComponents to views, otherwise the physic.getPhysics will not be updated
        setupPhysicsSystem()


        //add physic.getPhysics to s5 and attach a collisionCallback

        s5.addPhysicsComponent(layer = 4) {
            println("Ich ${owner.name} kollidiere mit ${it.owner.name}")
        }

        //add physic.getPhysics to s2, s3 and s4, but they should be solid (not dynamic) and have no callback
        addPhysicsComponentsTo(s2, s3, s4, isDynamic = false, layer = 3)


        val circleDynamic = Circle(radius = 100.0, Colors.RED).xy(50, 50)
        val solidRectStatic = SolidRect(75, 120, Colors.YELLOW).xy(100, 500)

        addChild(circleDynamic)
        addChild(solidRectStatic)

        circleDynamic.addPhysicsComponent(
            friction = Vec2(2.0f, 0.5f),
            isDynamic = true,
            layer = 1,
            coefficient = Vec2(120.0f, 120.0f))
        { other ->
            //das hier ist der Callback, der bei Kollision ausgeführt werden soll.
            //Das andere Element(other) ist das jeweilige Objekt, mit dem die Kollision stattfindet
            println("Hey, ich kollidiere gerade mit $other")
        }

        solidRectStatic.addPhysicsComponent(isDynamic = false, layer = 2, collisionCallback = {/* Nothing */})

        addUpdater {
            if (views.keys.justPressed(Key.UP)) {
                if (circleDynamic.physics?.isGrounded == true) {
                    sendPhysicsEvent(PhysicsDirection.UP, 400.0f, circleDynamic)
                }
            }
            if (views.keys[Key.LEFT]) {
                sendPhysicsEvent(PhysicsDirection.LEFT, 20.0f, circleDynamic)
            }
            if (views.keys[Key.RIGHT]) {
                sendPhysicsEvent(Vec2(20.0f, 0.0f), circleDynamic)
            }

            if (views.keys.justPressed(Key.W)) {
                if (s5.physics?.isGrounded == true) {
                    sendPhysicsEvent(PhysicsDirection.UP, 400.0f, s5)
                }
            }
            if (views.keys[Key.A]) {
                sendPhysicsEvent(PhysicsDirection.LEFT, 20.0f, s5)
            }
            if (views.keys[Key.D]) {
                sendPhysicsEvent(Vec2(20.0f, 0.0f), s5)
            }
        }
    }
}