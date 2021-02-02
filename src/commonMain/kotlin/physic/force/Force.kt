package physic.force

import physic.Physics

interface Force {
    fun updateForce(dt: Float, activePhysics: Physics)
}