package physic.force

import com.soywiz.korma.geom.Point
import org.jbox2d.common.Vec2
import physic.Physics

class Gravity(val acc: Vec2): Force {
    override fun updateForce(dt: Float, activePhysics: Physics) {
        activePhysics.addForce(acc)
    }
}

class Damping(): Force {
    override fun updateForce(dt: Float, activePhysics: Physics) {
        activePhysics.addForce(
            Vec2(
                (activePhysics.velocity.x * activePhysics.frictionX * -1).toFloat(),
                (activePhysics.velocity.y * activePhysics.frictionY * -1).toFloat()
            )
        )
    }
}