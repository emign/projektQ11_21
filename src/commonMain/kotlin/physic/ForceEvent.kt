package physic

import com.soywiz.korge.view.View
import eventController.Event
import eventController.eventController
import org.jbox2d.common.Vec2
import physic.internal.Physics
import physic.internal.PhysicsDirection

class ForceEvent(val force: Vec2, val receiver: Physics?) : Event {

    companion object {
        fun getForceFromDirection(direction: PhysicsDirection, magnitude: Float): Vec2 {
            return when (direction) {
                PhysicsDirection.DOWN -> {
                    Vec2(0.0f, magnitude)
                }
                PhysicsDirection.UP -> {
                    Vec2(0.0f, -magnitude)
                }
                PhysicsDirection.LEFT -> {
                    Vec2(-magnitude, 0.0f)
                }
                PhysicsDirection.RIGHT -> {
                    Vec2(magnitude, 0.0f)
                }
            }
        }
    }

    constructor(
        direction: PhysicsDirection,
        magnitude: Float,
        receiver: Physics?
    ) : this(ForceEvent.getForceFromDirection(direction, magnitude), receiver)
}

fun sendPhysicsEvent(force: Vec2, receiver: View) {
    if (receiver.physics != null) {
        eventController.send(ForceEvent(force, receiver.physics))
    }
}

fun sendPhysicsEvent(direction: PhysicsDirection, magnitude: Float, receiver: View) {
    if (receiver.physics != null) {
        eventController.send(ForceEvent(direction, magnitude, receiver.physics))
    }
}
