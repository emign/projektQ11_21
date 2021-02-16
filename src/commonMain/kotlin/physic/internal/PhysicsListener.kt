package physic.internal

import com.soywiz.kds.iterators.fastForEach
import org.jbox2d.common.Vec2
import physic.internal.forces.Damping
import physic.internal.forces.ForceRegistry
import physic.internal.forces.Gravity

/**
 * This is the place, where all the magic happens. The Listener updates all [Physics]-objects and performs collision-detection
 * @param gravityAcc The acceleration of the gravity as a [Vec2]. Usually 9.81 m/s^2
 */
object PhysicsListener {

    private var gravityAcc: Vec2 = Vec2(0.0f, 9.81f)

    internal operator fun invoke(gravity: Vec2) {
        gravityAcc = gravity
    }

    private var forceRegistry = ForceRegistry()
    private var gravity = Gravity(Vec2(gravityAcc))
    private var damping = Damping()

    /**
     * This list contains all [Physics]-objects that will be updated by the [Listener]
     */
    private val activeObjects: MutableList<Physics> = mutableListOf()

    /**
     * Add a new [Physics]-object to the active list. This object will be updated every frame
     */
    internal fun addPhysics(new: Physics) {
        activeObjects.add(new)
        forceRegistry.add(new, gravity)
        forceRegistry.add(new, damping)
    }

    /**
     * Remove a new [Physics]-object from the active list. This object will no longer be updated every frame
     */
    internal fun removePhysics(new: Physics) {
        activeObjects.remove(new)
        forceRegistry.remove(new, gravity)
        forceRegistry.remove(new, gravity)
    }

    /**
     * main updating method. This performs collision detection, force calculating, and position and velocity updating for every [Physics]-object in the [activeObjects]-list
     * @param dt the time elapsed since the last frame, measured in milliseconds
     */
    fun update(dt: Float) {
        forceRegistry.updateForces(dt)
        applyPhysics(dt)
        forceRegistry.zeroForces()
    }

    /**
     * Calculating velocities and positions for each physic.getPhysics-object added to the Listener. This also performs collision detection and resolution
     * @param dt The time elapsed since the last frame
     */
    private fun applyPhysics(dt: Float) {
        //milliseconds to seconds
        val ms = dt / 1000.0f
        activeObjects.fastForEach { activePhysics ->
            if (activePhysics.isDynamic) {
                activePhysics.lastVelocity = activePhysics.velocity
                activePhysics.lastPosition = activePhysics.position

                //update velocity
                activePhysics.velocity += activePhysics.force * ms
                //update position
                activePhysics.position.x += activePhysics.velocity.x * ms * activePhysics.coefficient.x
                activePhysics.position.y += activePhysics.velocity.y * ms * activePhysics.coefficient.y


                activePhysics.isGrounded = false

                //collision detection and solving
                activeObjects.filter { it != activePhysics && it.layer != activePhysics.layer}.fastForEach {
                    solveCollision(activePhysics, it)
                }

                //applying the modified position to the owner(SolidRect)

                //activePhysics.owner.lastPosition = Point(activePhysics.lastPosition.x, activePhysics.lastPosition.y)
                //activePhysics.owner.velocity = Point(activePhysics.velocity.x, activePhysics.velocity.y)
                //activePhysics.owner.lastVelocity = Point(activePhysics.lastVelocity.x, activePhysics.lastVelocity.y)
                /* TODO */activePhysics.owner.x = activePhysics.position.x.toDouble()
                /* TODO */activePhysics.owner.y = activePhysics.position.y.toDouble()
                activePhysics.force = Vec2(0.0f, 0.0f)
            }
        }
        //zero all forces, they have to be re-calculated every frame to have a correct simulation
        forceRegistry.zeroForces()
    }

    /**
     * This detects and solves the collisions between two [Physics]-objects.
     * First we use the [Physics.minkowskiDifference] to check whether they collide,
     * Then we calculate the solvingVector with [Rectangle.closestPointOnBoundsToPoint] to get the direction of the
     * collision. After that, we solve the collision by inverting velocities or setting [Physics.isGrounded] to true
     * At last, the custom collision [Physics.callback] is executed for both objects. It can contain extra code to execute on
     * Collision, for example taking damage.
     */
    private fun solveCollision(r1: Physics, r2: Physics) {
        val md = r1.minkowskiDifference(r2)
        if (md.x <= 0 && md.y <= 0 && md.x + md.width >= 0 && md.y + md.height >= 0) {
            val solvingVector = md.closestPointOnBoundsToPoint(Vec2(0.0f, 0.0f))
            if (solvingVector.x == 0.0f) {
                if (solvingVector.y > 0.00) {
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
                    r1.velocity.x *= -0.3f
                    if (r1.position.x + r1.width > r2.position.x) r1.position.x =
                        (r2.position.x - r1.width).toFloat()
                } else if (solvingVector.x < 0 && r1.velocity.x < 0) {
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