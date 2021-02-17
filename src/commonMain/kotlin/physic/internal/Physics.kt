package physic.internal

import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.View
import com.soywiz.korma.geom.Rectangle
import org.jbox2d.common.Vec2

/**
 * Rigidbody-class. A [Physics]-object can be moved by the [Listener], can be updated with forces by the [ForceRegistry]
 * and reacts to collision with other physic.getPhysics-objects. Add this to a [Listener] with [Listener.addPhysics]
 * @property owner the owner of a physic.getPhysics Object. A physic.getPhysics object is invisible, but can be attached to a [SolidRect], so the owner takes its position from the physic.getPhysics object
 * @property friction the coefficient of friction. This basically tells the [Damping]-force how to slow the object down. The higher the coefficient is, the faster it will be slowed down. Has a x- and y-Value
 * @property isDynamic just a boolean value for checking whether the object should move or not. This is like Box2D's static and dynamic
 * @property coefficient this is used internal for calculating pixels from meters. The higher this is, the faster the object will move, but collision detection becomes more inaccurate.
 * @property callback a custom callback-function which is executed every time a collision occurs. It takes another [Physics]-object as a parameter: The object on this object collides with
 */

class Physics(
        val owner: View,
        var friction: Vec2,
        var isDynamic: Boolean,
        val layer: Int,
        val coefficient: Vec2 = Vec2(120f, 120f),
        val callback: Physics.(Physics) -> Unit
) {

    var position = Vec2(owner.pos.x.toFloat(), owner.pos.y.toFloat())
    var lastPosition = position
    var velocity: Vec2 = Vec2()
    var lastVelocity = velocity

    val x: Float get() = position.x
    val y: Float get() = position.y
    val width = if (owner is SolidRect) owner.width else if (owner is Circle) owner.radius*2 else error("Circle or Solidrect")
    val height = if (owner is SolidRect) owner.height else if (owner is Circle) owner.radius*2 else error("Circle or Solidrect")

    /**
     * Sum of all forces acting on this object at the moment. Is cleared and re-calculated every frame
     */
    var force: Vec2 = Vec2(0.0f, 0.0f)

    /**
     * Adds a force to the [force] variable of this object
     */
    fun addForce(newForce: Vec2) {
        this.force = this.force + newForce
    }

    /**
     * is the [Physics]-object on the ground? If yes, then we are allowed to jump! ;)
     */
    var isGrounded: Boolean = false


    /**
     * calculates the minkowski difference of this Physics object and another one. This is used for collision detection.
     * The concept of Minkowski Difference is basically to subtract every point from a [Rectangle] from every point of another
     * [Rectangle]. All those calculated points together combined creates a new [Rectangle]. If the original rectangles intersect, it means
     * that they have one or more points in common. But because of subtracting every point from every point, the overlapping points
     * cancel out to zero. So we just have to check if the origin (0, 0) is located inside of the resulting minkowski-rectangle, and we now
     * that the two original physic.getPhysics objects are colliding. If this happens, we can just calculate the [closestPointOnBoundsToPoint]-method
     * and take the minkowski-rectangle and the origin(0, 0) as paramter. This tells us from which direction the original [Physics]-objects
     * collided and is super useful for collision resolution
     * See [Listener.solveCollision] for the solving part
     * */
    internal fun minkowskiDifference(other: Physics): Rectangle {
        val x = this.position.x - (other.position.x + other.width)
        val y = this.position.y - (other.position.y + other.height)
        val width = this.width + other.width
        val height = this.height + other.height
        return Rectangle(x = x.toFloat(), y = y.toFloat(), width = width.toFloat(), height = height.toFloat())
    }
}
