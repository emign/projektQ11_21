package physic.internal.forces

import com.soywiz.kds.iterators.fastForEach
import org.jbox2d.common.Vec2
import physic.internal.Physics

/**
 * This class manages all [Physics]-objects and is responsible for applying the forces to them. Each [Listener]
 * has one [ForceRegistry] for updating the forces. Then the [Listener] updates positions and velocities based on the
 * forces.
 */
class ForceRegistry {

    private val connections: MutableList<ForceConnection> = mutableListOf()

    /**
     * Adds a [Physics] object to the list and add a [Force] to it so that the [ForceRegistry] updates this object with the specified force
     * If you want to have multiple forces acting on a [Physics]-object, you have to call this method for each force.
     * @param activePhysics The physic.getPhysics-object which will be added to the list and updated every frame
     * @param force The [Force] which will be applied on the object every frame
     */
    fun add(activePhysics: Physics, force: Force) {
        val fr = ForceConnection(force, activePhysics)
        connections.add(fr)
    }

    /**
     * Removes a [Force] from a [Physics]-object. The [ForceRegistry] stops applying the force to the object every frame
     * @param activePhysics the object, from which the force will be removed
     * @param force The [Force] that will be removed and no longer updated
     */
    fun remove(activePhysics: Physics, force: Force) {
        val fr = ForceConnection(force, activePhysics)
        connections.remove(fr)
    }

    /**
     * Clears all forces in this [ForceRegistry]. The registry stops updating and no force is applied on any physic.getPhysics object,
     * which was added to this registry. All [Force]s and [Physics]-objects will be removed from the list
     */
    fun clear() {
        connections.clear()
    }

    /**
     * Clears all [Force]s of each [Physics]-object in this [ForceRegistry]. The registry will continue updating, but
     * but all forces are set to 0
     */
    fun zeroForces() {
        connections.fastForEach {
            it.activePhysics.force = Vec2(0.0f, 0.0f)
        }
    }

    /**
     * Updates all [Physics]-objects. Just calling the [Force.updateForce] method for each pair of [Physics]-objet and [Force]
     * @param dt The time elapsed since the last frame
     */
    fun updateForces(dt: Float) {
        connections.fastForEach {
            it.force.updateForce(dt, it.activePhysics)
        }
    }
}