package physic.force

import com.soywiz.kds.iterators.fastForEach
import com.soywiz.korma.geom.Point
import org.jbox2d.common.Vec2
import physic.Physics

class ForceRegistry {

    private val connections: MutableList<ForceConnection> = mutableListOf()

    fun add(activePhysics: Physics, force: Force) {
        val fr = ForceConnection(force, activePhysics)
        connections.add(fr)
    }

    fun remove(activePhysics: Physics, force: Force) {
        val fr = ForceConnection(force, activePhysics)
        connections.remove(fr)
    }

    fun clear() {
        connections.clear()
    }

    fun zeroForces() {
        connections.fastForEach {
            it.activePhysics.force = Vec2(0.0f, 0.0f)
        }
    }

    fun updateForces(dt: Float) {
        connections.fastForEach {
            it.force.updateForce(dt, it.activePhysics)
        }
    }
}