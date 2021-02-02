package physic

import com.soywiz.korge.view.SolidRect
import org.jbox2d.collision.AABB
import org.jbox2d.common.MathUtils
import org.jbox2d.common.Vec2
import kotlin.math.abs
import kotlin.math.tan

class Physics(
    val owner: SolidRect,
    val listener: Listener,
    info: PhysicsInfo
) {

    class PhysicsInfo(
        val width: Double,
        val height: Double,
        var position: Vec2,
        var velocity: Vec2,
        var friction: Vec2,
        var kinematic: Boolean,
        val xCoefficient: Double,
        val yCoefficient: Double,
        val callback: (Physics, Physics) -> Unit
    )

    var position = info.position
    var lastPosition = position
    var velocity = info.velocity
    var lastVelocity = velocity
    var frictionX = info.friction.x
    var frictionY = info.friction.y
    var width: Double = info.width
    var height: Double = info.height

    val callback = info.callback

    val x: Float get() = position.x
    val y: Float get() = position.y

    var force: Vec2 = Vec2(0.0f, 0.0f)
    val xCoefficient: Float = info.xCoefficient.toFloat()
    val yCoefficient: Float = info.yCoefficient.toFloat()

    var isGrounded: Boolean = false
    var isKinematic = info.kinematic

    class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
        fun closestPointOnBoundsToPoint(point: Vec2): Vec2 {
            var minDist = abs(point.x - this.x)
            var boundsPoint = Vec2(-minDist, 0.0f)
            if (abs(this.x + this.width - point.x) < minDist) {
                minDist = abs(this.x + this.width - point.x).toFloat()
                boundsPoint = Vec2(minDist, 0.0f)
            }
            if (abs(this.y + this.height - point.y) < minDist) {
                minDist = abs(this.y + this.height - point.y).toFloat()
                boundsPoint = Vec2(0.0f, minDist)
            }
            if (abs(this.y - point.y) < minDist) {
                minDist = abs(this.y - point.y)
                boundsPoint = Vec2(0.0f, -minDist)
            }
            return boundsPoint
        }
    }


    fun minkowskiDifference(other: Physics): Rect {
        val x = this.position.x - (other.position.x + other.width)
        val y = this.position.y - (other.position.y + other.height)
        val width = this.width + other.width
        val height = this.height + other.height
        return Rect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    }

    fun addForce(newForce: Vec2) {
        this.force = this.force + newForce
    }
}
