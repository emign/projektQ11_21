package physic.internal.forces

import org.jbox2d.common.Vec2
import physic.internal.Physics

/**
 * Gravity force. Implements [Force] and overrides the [Force.updateForce] method
 */
class Gravity(val acc: Vec2): Force {
    override fun updateForce(dt: Float, activePhysics: Physics) {
        activePhysics.addForce(acc)
    }
}

/**
 * Damping force. Implements [Force] and overrides the [Force.updateForce] method
 * Damping is basically the air-resistance and friction.
 * Responsible for slowing down the [Physics]-objects if no other forces act on them
 */
class Damping(): Force {
    override fun updateForce(dt: Float, activePhysics: Physics) {
        activePhysics.addForce(
            Vec2(
                (activePhysics.velocity.x * activePhysics.friction.x * -1).toFloat(),
                (activePhysics.velocity.y * activePhysics.friction.y * -1).toFloat()
            )
        )
    }
}