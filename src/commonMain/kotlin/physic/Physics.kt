package physic

import com.soywiz.korge.view.SolidRect
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
        val yCoefficient: Double
        //var gravity: Double,
        //val callback: (Physics, Physics) -> Unit
    )

    val aabb: AABB = AABB(info.position.x, info.position.y, info.width, info.height)

    var position = info.position
    var lastPosition = position
    var velocity = info.velocity
    var lastVelocity = velocity
    var frictionX = info.friction.x
    var frictionY = info.friction.y

    var force: Vec2 = Vec2(0.0f, 0.0f)
    val xCoefficient: Double = info.xCoefficient
    val yCoefficient: Double = info.yCoefficient
    //val threshold: Double = 0.01

    var isGrounded: Boolean = false
    var isKinematic = info.kinematic

    class AABB(var x: Float, var y: Float, var width: Double, var height: Double) {
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

        fun getRayIntersectionFractionOfFirstRay(originA: Vec2, endA: Vec2, originB: Vec2, endB: Vec2): Float {
            val r = endA - originA
            val s = endB - originB

            val numerator: Float = (originB - originA) * r
            val denominator: Float = r * s

            if (numerator == 0f && denominator == 0f) {
                return Float.POSITIVE_INFINITY
            }
            if (denominator == 0f) {
                return Float.POSITIVE_INFINITY
            }

            val u: Float = numerator / denominator
            val t: Float = ((originB - originA) * s) / denominator
            if ((t >= 0) && (t <= 1) && (u >= 0) && (u <= 1)) {
                return t
            }
            return Float.POSITIVE_INFINITY
        }

        fun getRayIntersectionFraction(origin: Vec2, direction: Vec2): Float {
            val end = origin + direction
            var minT = getRayIntersectionFractionOfFirstRay(origin, end, Vec2(this.x, this.y), Vec2((this.x).toFloat(),
                (this.y + this.height).toFloat()
            ))

            var xx: Float = getRayIntersectionFractionOfFirstRay(origin, end, Vec2(this.x,
                (this.y + this.height).toFloat()), Vec2((this.x + this.width).toFloat(),
                (this.y + this.height).toFloat()))
            if (xx < minT) {
                minT = x
            }

            xx = getRayIntersectionFractionOfFirstRay(origin, end, Vec2(
                (this.x + this.width).toFloat(),
                (this.y + this.height).toFloat()), Vec2((this.x + this.width).toFloat(),
                (this.y).toFloat()))
            if (xx < minT) {
                minT = x
            }

            xx= getRayIntersectionFractionOfFirstRay(origin, end, Vec2(
                (this.x + this.width).toFloat(),
                (this.y).toFloat()), Vec2((this.x).toFloat(),
                (this.y).toFloat()))
            if (xx < minT) {
                minT = x
            }

            return minT
        }
    }

    /*fun update(dt: Double) {
        if (info.mass != 0.0) {
            force.y += info.gravity
            aabb.x = info.left
            aabb.y = info.top
            aabb.width = info.right - info.left
            aabb.height = info.bottom - info.top
            //Calculate velocity
            info.velocity.x += force.x * dt
            info.velocity.y += force.y * dt

            checkCollisions()

            //Update position
            info.position.x += info.velocity.x * dt * xCoefficient
            info.position.y += info.velocity.y * dt * yCoefficient

            owner.x = info.position.x
            owner.y = info.position.y
            force = Point(0.0)
        }
    }*/

    /*fun checkCollisions() {
        Listener.rectangles.filter { it != this }.forEach {
            onCollisionWithStaticView(it)
        }
    }*/

    fun minkowskiDifference(other: Physics): AABB {
        val x = this.aabb.x - (other.aabb.x + other.aabb.width)
        val y = this.aabb.y - (other.aabb.y + other.aabb.height)
        val width = this.aabb.width + other.aabb.width
        val height = this.aabb.height + other.aabb.height
        return AABB(x.toFloat(), y.toFloat(), width, height)
    }

    fun onCollisionWithStaticView(other: Physics, dt: Float, xCoefficient: Float, yCoefficient: Float) {
        val md = this.minkowskiDifference(other)
        if (md.x <= 0 && md.y <= 0 && md.x + md.width >= 0 && md.y + md.height >= 0) {
            val solvingVector = md.closestPointOnBoundsToPoint(Vec2(0.0f, 0.0f))
            if (solvingVector.x == 0.0f) {
                if (solvingVector.y > 0.00) {
                    print("BODEN")
                    if (velocity.y > 0) velocity.y = 0.0f
                    isGrounded = true
                    if (position.y + this.aabb.height > other.position.y) position.y =
                        (other.position.y - aabb.height).toFloat()
                } else {
                    velocity.y = 2.0f
                    force.y = listener.gravityAcc.y
                }
            } else if (solvingVector.y == 0.0f) {
                if (solvingVector.x > 0.00 && velocity.x > 0) {
                    println("Kollidiert links")
                    velocity.x *= -0.3f
                    if (position.x + aabb.width > other.position.x) position.x =
                        (other.position.x - aabb.width).toFloat()
                } else if (solvingVector.x < 0 && velocity.x < 0) {
                    println("Kollidiert rechts")
                    velocity.x *= -0.3f
                    if (position.x < other.aabb.width + other.position.x) position.x =
                        (other.position.x + other.aabb.width).toFloat()
                }
            }
        } else {
            val relV = Vec2((this.velocity.x - other.velocity.x) * dt, (this.velocity.y - other.velocity.y) * dt)
            val h = md.getRayIntersectionFraction(Vec2(0.0f, 0.0f), relV)
            if (h < Float.POSITIVE_INFINITY) {
                this.position.x += this.velocity.x * dt * h
                this.position.y += this.velocity.y * dt * h
                other.position.x += other.velocity.x * dt * h
                other.position.y += other.velocity.y * dt * h

                relV.normalize()
                val tangent = relV.getTangent()
                this.velocity = tangent * Vec2.dot(this.velocity, tangent)
                other.velocity = tangent * Vec2.dot(other.velocity, tangent)
            } else {
                // no intersection, move it along
                this.position += this.velocity * dt;
                other.position += other.velocity * dt;
            }

        }
    }

    fun addForce(newForce: Vec2) {
        this.force = this.force + newForce
    }
}

    operator fun Vec2.plus(new: Vec2): Vec2 {
        return Vec2(x + new.x, y + new.y)
    }

    operator fun Vec2.times(value: Number): Vec2 {
        return Vec2(this.x * value.toFloat(), this.y * value.toFloat())
    }

    operator fun Vec2.times(new: Vec2): Float {
    return this.x * new.x + this.y * new.y
    }

    operator fun Vec2.minus(new: Vec2): Vec2 {
        return Vec2(this.x - new.x, this.y - new.y)
    }

    fun Vec2.getTangent(): Vec2 {
        return Vec2(-this.y, this.x)
    }
