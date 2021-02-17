package physic.internal.forces

import physic.internal.Physics

/**
 * Basic interface for all different forces. They are all stored in [Forces.kt]. The two most important ones are
 * Gravity and Damping(Air Resistance)
 */
interface Force {
    /**
     * Updates the force and manipulates the Physics object
     * Has to be overridden in all force-classes
     * @param activePhysics the object on which the force will be applied
     */
    fun updateForce(dt: Float, activePhysics: Physics)
}