package physic

import com.soywiz.kds.iterators.fastForEach
import org.jbox2d.common.Vec2
import physic.force.Damping
import physic.force.ForceRegistry
import physic.force.Gravity

class Listener(val gravityAcc: Vec2) {


    private var forceRegistry = ForceRegistry()
    private var gravity = Gravity(Vec2(gravityAcc))
    private var damping = Damping()

    val activeObjects: MutableList<Physics> = mutableListOf()

    enum class PointType(val number: Int) { BEGIN(0), END(1) }
    class EndPoint(val type: PointType, val value: Double, val aabbIndex: Int)
    class Pair(val r1: Physics, val r2: Physics)

    var xEndPoints: MutableList<EndPoint> = mutableListOf()
    val THRESHOLD: Double = 0.02


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
        forceRegistry.updateForces(dt)
        applyPhysics(dt)
        forceRegistry.zeroForces()
    }

    fun applyPhysics(dt: Float) {
        val ms = dt / 1000.0f
        activeObjects.fastForEach { activePhysics ->
            if (activePhysics.isKinematic) {
                activePhysics.lastVelocity = activePhysics.velocity
                activePhysics.lastPosition = activePhysics.position

                activePhysics.velocity += activePhysics.force * ms
                activePhysics.position.x += activePhysics.velocity.x * ms * activePhysics.xCoefficient
                activePhysics.position.y += activePhysics.velocity.y * ms * activePhysics.yCoefficient


                activePhysics.isGrounded = false

                activeObjects.filter { it != activePhysics }.fastForEach {
                    println(it.owner.name)
                    solveCollision(activePhysics, it)
                }

                //activePhysics.owner.lastPosition = Point(activePhysics.lastPosition.x, activePhysics.lastPosition.y)
                //activePhysics.owner.velocity = Point(activePhysics.velocity.x, activePhysics.velocity.y)
                //activePhysics.owner.lastVelocity = Point(activePhysics.lastVelocity.x, activePhysics.lastVelocity.y)
                /* TODO */activePhysics.owner.x = activePhysics.position.x.toDouble()
                /* TODO */activePhysics.owner.y = activePhysics.position.y.toDouble()
                activePhysics.force = Vec2(0.0f, 0.0f)
            }
        }
        forceRegistry.zeroForces()
    }

    fun solveCollision(r1: Physics, r2: Physics) {
        val md = r1.minkowskiDifference(r2)
        if (md.x <= 0 && md.y <= 0 && md.x + md.width >= 0 && md.y + md.height >= 0) {
            val solvingVector = md.closestPointOnBoundsToPoint(Vec2(0.0f, 0.0f))
            if (solvingVector.x == 0.0f) {
                if (solvingVector.y > 0.00) {
                    print("BODEN")
                    if (r1.velocity.y > 0) r1.velocity.y = 0.0f
                    r1.isGrounded = true
                    if (r1.position.y + r1.height > r2.position.y) r1.position.y =
                        (r2.position.y - r1.height).toFloat()
                } else {
                    r1.velocity.y = 2.0f
                    r1.force.y = gravityAcc.y
                }
            } else if (solvingVector.y == 0.0f) {
                if (solvingVector.x > 0.00 && r1.velocity.x > 0) {
                    println("Kollidiert links")
                    r1.velocity.x *= -0.3f
                    if (r1.position.x + r1.width > r2.position.x) r1.position.x =
                        (r2.position.x - r1.width).toFloat()
                } else if (solvingVector.x < 0 && r1.velocity.x < 0) {
                    println("Kollidiert rechts")
                    r1.velocity.x *= -0.3f
                    if (r1.position.x < r2.width + r2.position.x) r1.position.x =
                        (r2.position.x + r2.width).toFloat()
                }
            }
            r1.callback(r1, r2)
            r2.callback(r2, r1)
        }
    }

}