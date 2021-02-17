package actor

import com.soywiz.korge.view.Container
import com.soywiz.korma.geom.Point
import org.jbox2d.common.Vec2
import physic.internal.Physics

/**
 * An actor is basically an object which you can see on screen (Player, Enemy, Ground, ...)
 */

abstract class Actor(val par: Container) : Container() {

    var position: Point = Point(this.y, this.y)
    var lastPosition: Point = position
    var newScale: Double = this.scale
    var velocity: Point = Point(0.0, 0.0)
    var lastVelocity: Point = Point(0.0, 0.0)

    var timer: Int = 0
    var dead: Boolean = false

    abstract fun onCreate()
    abstract fun onExecute(dt: Double)
    abstract fun onDelete()

    abstract fun updateGraphics()

    fun initPhysics(
        isDynamic: Boolean,
        friction: Vec2 = Vec2(2.0f, 0.5f),
        coefficient: Vec2 = Vec2(120.0f, 120.0f),
        collisionCallback: Physics.(Physics) -> Unit = {}
    ) {
        //par.physic.addPhysicsComponent(this, friction, isDynamic, coefficient, collisionCallback)
    }

    abstract fun kill()
}