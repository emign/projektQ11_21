package physic

import actor.MovingActor
import com.soywiz.korma.geom.Point
import kotlin.math.abs

class Physics(val owner: MovingActor, val gravity: Double) {

    val xCoefficient: Double = 1.0
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
    }

}