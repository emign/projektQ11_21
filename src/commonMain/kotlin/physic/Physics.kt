package physic

import com.soywiz.korge.view.SolidRect
import org.jbox2d.common.Vec2
import kotlin.math.abs

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
        fun closestPointOnBoundsToPoint(point: Vec2) : Vec2 {
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

    fun onCollisionWithStaticView(other: Physics) {
        val viewComponent = this.aabb
        val md = this.minkowskiDifference(other)
        if (md.x <= 0 && md.y <= 0 && md.x + md.width >= 0 && md.y + md.height >= 0) {
            val solvingVector = md.closestPointOnBoundsToPoint(Vec2(0.0f, 0.0f))
            if (solvingVector.x == 0.0f) {
                if (solvingVector.y > 0.00) {
                    print("BODEN")
                    if (velocity.y > 0) velocity.y = 0.0f
                    isGrounded = true
                    if (position.y + this.aabb.height > other.position.y) position.y = (other.position.y - aabb.height).toFloat()
                } else {
                    velocity.y = 2.0f
                    force.y = listener.gravityAcc.y
                }
            } else if (solvingVector.y == 0.0f) {
                if (solvingVector.x > 0.00 && velocity.x > 0) {
                    println("Kollidiert links")
                    velocity.x *= -0.3f
                    if (position.x + aabb.width > other.position.x) position.x = (other.position.x - aabb.width).toFloat()
                } else if (solvingVector.x < 0 && velocity.x < 0) {
                    println("Kollidiert rechts")
                    velocity.x *= -0.3f
                    if (position.x < other.aabb.width + other.position.x) position.x = (other.position.x + other.aabb.width).toFloat()
                }
            }
        }

    }

    fun addForce(newForce: Vec2) {
        this.force = this.force + newForce
    }


/*val xCoefficient: Double = 1.0
val yCoefficient: Double = 0.01
val threshold: Double = 0.01

fun update(dt: Double, maxSpeedX: Double = 0.0, speedStepX: Double = 0.0, increaseX: Boolean = false) {
    when(owner.getCurrentState()) {
        owner.idleState -> {
            verticalMovement(dt)
        }
        owner.walkState -> {
            horizontalMovement(dt, maxSpeedX, speedStepX, increaseX)
        }
        owner.turnState -> {
            owner.resetXPhysics()
        }
        owner.jumpState -> {
            owner.velocity.y = -1.0 * owner.actorXmlData.jumpHeight
            verticalMovement(dt)
            if (abs(owner.velocity.x) > threshold) {
                horizontalMovement(dt, maxSpeedX, speedStepX, increaseX)
            }
        }
        owner.normalAttackState -> {
            verticalMovement(dt)
        }
        owner.rangedAttackState -> {
            verticalMovement(dt)
        }
        owner.specialAttackState -> {
            owner.resetXPhysics()
            owner.resetYPhysics()
        }
        owner.getDamageState -> {
            verticalMovement(dt)
        }
    }
}

fun horizontalMovement(dt: Double, maxSpeed: Double, speedStep: Double, increase: Boolean) {
    owner.lastVelocity.x = owner.velocity.x
    owner.lastPosition.x = owner.position.x

    owner.position.x += (owner.lastVelocity.x + owner.velocity.x) * 0.5 * dt * xCoefficient

    if (increase && owner.velocity.x < maxSpeed) {
        owner.velocity.x += speedStep
    }
}

fun verticalMovement(dt: Double) {
    owner.lastVelocity.y = owner.velocity.y
    owner.lastPosition.y = owner.position.y

    owner.position.y += (owner.lastVelocity.y + owner.velocity.y) * 0.5 * dt * yCoefficient

    owner.velocity.y += gravity
}*/

}

operator fun Vec2.plus(new: Vec2): Vec2 {
    return Vec2(x + new.x, y + new.y)
}

operator fun Vec2.times(value: Number): Vec2 {
    return Vec2(this.x * value.toFloat(), this.y * value.toFloat())
}
