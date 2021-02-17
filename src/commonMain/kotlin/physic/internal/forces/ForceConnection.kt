package physic.internal.forces

import physic.internal.Physics

/**
 * Acts as a wrapper for combinig a [Force] and a [Physics]-object on which the force will be applied
 * This is only used internal
 */
class ForceConnection(val force: Force, val activePhysics: Physics) {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != ForceConnection::class) return false
        val obj = other as ForceConnection

        return obj.activePhysics == this.activePhysics && obj.force == this.force
    }

    override fun hashCode(): Int {
        var result = force.hashCode()
        result = 31 * result + activePhysics.hashCode()
        return result
    }
}