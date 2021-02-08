import com.soywiz.kds.getExtra
import com.soywiz.kds.iterators.fastForEach
import com.soywiz.kds.setExtra
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.addUpdater
import org.jbox2d.common.Vec2
import physic.Physics
import physic.PhysicsListener



/**
 * Attach a [Listener] to a [Container]. The listener will automatically be updated every frame with the correct deltaTime
 * and updates all physics contained in it. To add a physics object to a [Container], see [SolidRect.addPhysicsComponent]
 * @param gravity The gravity for the whole physics system as [Vec2]. By default it is Vec2(0f, 9.81f)
 */
fun Container.setupPhysicsSystem(gravity: Vec2 = Vec2(0f, 9.81f)) {
    PhysicsListener(gravity)
    this.addUpdater {
        PhysicsListener.update(it.milliseconds.toFloat())
    }
}



/**
 * Adds a new [Physics]-object to the listener of the container.
 * Be sure to first initialize a [Listener] to the Container with [Container.setupPhysicsSystem], otherwise nothing will be updated
 * @param friction The friction vector (used for air resistance). By default it's Vec2(2.0f, 0.5f)
 * @param isDynamic Should the [Physics]-object move or be static? Specify it here!
 * @param coefficient The coefficient for calculating the pixel distance from meters. The higher you set it, the faster the objects will move. By default 120.0
 * @param collisionCallback A custom callback which is executed when a collision with this [SolidRect] occurs. It takes another [Physics]-object as parameter for the collision partner
 */
fun SolidRect.addPhysicsComponent(
    friction: Vec2 = Vec2(2.0f, 0.5f),
    isDynamic: Boolean = true,
    coefficient: Vec2 = Vec2(120.0f, 120.0f),
    collisionCallback: Physics.(Physics) -> Unit = {}
) {
    val physics = Physics(this, friction, isDynamic, coefficient, collisionCallback)
    this.setExtra("physics", physics)
    PhysicsListener.addPhysics(physics)
}



/**
 * Adds new [Physics]-objects to the listener. For each of the objects the function [SolidRect.addPhysicsComponent] is called.
 * Remember that all parameters passed in as [friction], [isDynamic], ... will be taken for every object passed in to [owners]
 * @param owners a vararg parameter for all the [SolidRect]s that will be added a Physics-component.
 * For the other parameters see [SolidRect.addPhysicsComponent] for documentation
 */
fun addPhysicsComponentsTo(
    vararg owners: SolidRect,
    friction: Vec2 = Vec2(2.0f, 0.5f),
    isDynamic: Boolean = true,
    coefficient: Vec2 = Vec2(120.0f, 120.0f),
    collisionCallback: Physics.(Physics) -> Unit = {}
) {
    owners.fastForEach {
        it.addPhysicsComponent(friction, isDynamic, coefficient, collisionCallback)
    }
}



/**
 * Holds a [Physics]-object for each [SolidRect]. Returns null if this solidrect has no Physics-object. Create it with [Container.addPhysicsComponent]
 */
val SolidRect.physics: Physics?
    get() {
        val p = getExtra("physics")
        return if (p is Physics) p as Physics
        else null
    }