package physic

import com.soywiz.kds.iterators.fastForEach
import com.soywiz.korma.geom.Point
import org.jbox2d.common.Vec2
import physic.force.Damping
import physic.force.ForceRegistry
import physic.force.Gravity

class Listener(val gravityAcc: Vec2) {


    private var forceRegistry = ForceRegistry()
    private var gravity = Gravity(Vec2(gravityAcc))
    private var damping = Damping()

    val THRESHOLD: Double = 0.02

    enum class PointType(val number: Int) { BEGIN(0), END(1) }
    class EndPoint(val type: PointType, val value: Double, val aabbIndex: Int)
    class Pair(val r1: Physics, val r2: Physics)

    var xEndPoints: MutableList<EndPoint> = mutableListOf()
    val activeObjects: MutableList<Physics> = mutableListOf()

    fun addPhysics(new: Physics) {
        activeObjects.add(new)
        forceRegistry.add(new, gravity)
        forceRegistry.add(new, damping)
    }

    fun removePhysics(new: Physics) {
        activeObjects.remove(new)
        forceRegistry.remove(new, gravity)
        forceRegistry.remove(new, gravity)
    }

    fun update(dt: Float) {
        val possCollisions = updateX()
        forceRegistry.updateForces(dt)
        applyPhysics(dt, possCollisions)
    }

    fun applyPhysics(dt: Float, pairs: MutableList<Pair>) {
        val ms = dt/1000.0f
        activeObjects.fastForEach { activePhysics ->
            if (activePhysics.isKinematic) {
                //activePhysics.lastVelocity = activePhysics.velocity
                //activePhysics.lastPosition = activePhysics.position

                activePhysics.velocity += activePhysics.force * ms

                activePhysics.position.x += (activePhysics.velocity.x * ms * activePhysics.xCoefficient).toFloat()
                activePhysics.position.y += (activePhysics.velocity.y * ms * activePhysics.yCoefficient).toFloat()

                //activePhysics.owner.lastPosition = Point(activePhysics.lastPosition.x, activePhysics.lastPosition.y)
                //activePhysics.owner.velocity = Point(activePhysics.velocity.x, activePhysics.velocity.y)
                //activePhysics.owner.lastVelocity = Point(activePhysics.lastVelocity.x, activePhysics.lastVelocity.y)
                activePhysics.force = Vec2(0.0f, 0.0f)
            }
        }
        pairs.fastForEach { pair ->
            pair.r1.isGrounded = false
            pair.r1.onCollisionWithStaticView(pair.r2)
        }

        activeObjects.fastForEach { activePhysics ->
            activePhysics.aabb.x = activePhysics.position.x
            activePhysics.aabb.y = activePhysics.position.y
            /* TODO */activePhysics.owner.x = activePhysics.position.x.toDouble()
            activePhysics.owner.y = activePhysics.position.y.toDouble()
        }
        forceRegistry.zeroForces()
    }

   fun updateX(): MutableList<Pair> {
        xEndPoints = mutableListOf()
        for (i in activeObjects.indices) {
            xEndPoints.add(2 * i, EndPoint(PointType.BEGIN, activeObjects[i].aabb.x.toDouble(), i))
            xEndPoints.add(2 * i + 1, EndPoint(PointType.END, activeObjects[i].aabb.x + activeObjects[i].aabb.width, i))
        }
        xEndPoints = insertionSort(xEndPoints)
        return sweepX()
    }

    private fun sweepX(): MutableList<Pair> {
        xEndPoints = insertionSort(xEndPoints)
        val activeList = mutableListOf<EndPoint>()
        val collisions = mutableListOf<Pair>()
        for (i in 0 until xEndPoints.size - 1) {
            if (xEndPoints[i].type == PointType.BEGIN) {
                activeList.add(xEndPoints[i])
            } else {
                if (xEndPoints[i + 1].value - xEndPoints[i].value < THRESHOLD) {
                    collisions.add(Pair(activeObjects[xEndPoints[i].aabbIndex], activeObjects[xEndPoints[i + 1].aabbIndex]))
                }
                activeList.remove(activeList.filter { it.aabbIndex == xEndPoints[i].aabbIndex }[0])
                activeList.forEach {
                    collisions.add(Pair(activeObjects[xEndPoints[i].aabbIndex], activeObjects[it.aabbIndex]))
                }
            }
        }
        return collisions
    }

    private fun insertionSort(items: MutableList<EndPoint>): MutableList<EndPoint> {
        if (items.isEmpty() || items.size < 2) {
            return items
        }
        for (count in 1 until items.size) {
            val item = items[count]
            var i = count
            while (i > 0 && item.value < items[i - 1].value) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }
        return items
    }

}