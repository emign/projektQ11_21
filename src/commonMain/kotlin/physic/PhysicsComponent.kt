package physic

import com.soywiz.kds.getExtra
import com.soywiz.kds.iterators.fastForEach
import com.soywiz.kds.setExtra
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.View
import com.soywiz.korge.view.addUpdater
import org.jbox2d.common.Vec2
import physic.internal.Physics
import physic.internal.PhysicsListener


/**
 * Attach a [Listener] to a [Container]. The listener will automatically be updated every frame with the correct deltaTime
 * and updates all physic.getPhysics contained in it. To add a physic.getPhysics object to a [Container], see [View.addPhysicsComponent]
 * @param gravity The gravity for the whole physic.getPhysics system as [Vec2]. By default it is Vec2(0f, 9.81f)
 */
fun Container.setupPhysicsSystem(gravity: Vec2 = Vec2(0f, 9.81f)) {
    if (!PhysicsListener.running) {
        PhysicsListener(gravity)
        PhysicsListener.running = true
        this.addUpdater {
            PhysicsListener.update(it.milliseconds.toFloat())
        }
    }
}


/**
 * Adds a new [Physics]-object to the [PhysicsListener].
 * Be sure to initialize the listener to the Container with [Container.setupPhysicsSystem] first, otherwise nothing will be updated
 * @param friction The friction vector (used for air resistance). By default it's Vec2(2.0f, 0.5f)
 * @param isDynamic Should the [Physics]-object move or be static? Specify it here!
 * @param layer The layer on which the physic.getPhysics object should be placed. All objects on the same layer can not collide with each other!
 * @param coefficient The coefficient for calculating the pixel distance from meters. The higher you set it, the faster the objects will move. By default 120.0
 * @param collisionCallback A custom callback which is executed when a collision with this [View] occurs. It takes another [Physics]-object as parameter for the collision partner
 */
fun View.addPhysicsComponent(
    friction: Vec2 = Vec2(2.0f, 0.5f),
    isDynamic: Boolean = true,
    layer: Int,
    coefficient: Vec2 = Vec2(120.0f, 120.0f),
    collisionCallback: Physics.(Physics) -> Unit = {}
) {
    val physics = Physics(this, friction, isDynamic, layer, coefficient, collisionCallback)
    this.setExtra("physicComponent", physics)
    PhysicsListener.addPhysics(physics)
}


/**
 * Adds new [Physics]-objects to the [PhysicsListener]. For each of the objects the function [View.addPhysicsComponent] is called.
 * Remember that all parameters passed in as [friction], [isDynamic], ... will be taken for every object passed in to [owners]
 * @param owners a vararg parameter for all the [View]s that will be added a Physics-component.
 * @param friction The friction vector (used for air resistance). By default it's Vec2(2.0f, 0.5f)
 * @param isDynamic Should the [Physics]-object move or be static? Specify it here!
 * @param layer The layer on which the physic.getPhysics object should be placed. All objects on the same layer can not collide with each other!
 * @param coefficient The coefficient for calculating the pixel distance from meters. The higher you set it, the faster the objects will move. By default 120.0
 * @param collisionCallback A custom callback which is executed when a collision with this [View] occurs. It takes another [Physics]-object as parameter for the collision partner

 */
fun addPhysicsComponentsTo(
    vararg owners: SolidRect,
    friction: Vec2 = Vec2(2.0f, 0.5f),
    isDynamic: Boolean = true,
    layer: Int,
    coefficient: Vec2 = Vec2(120.0f, 120.0f),
    collisionCallback: Physics.(Physics) -> Unit = {}
) {
    owners.fastForEach {
        it.addPhysicsComponent(friction, isDynamic, layer, coefficient, collisionCallback)
    }
}


/**
 * Holds a [Physics]-object for each [View]. Returns null if this solidrect has no Physics-object. Create it with [View.addPhysicsComponent]
 */
val View.physics: Physics?
    get() {
        val p = getExtra("physicComponent")
        return if (p is Physics) p as Physics
        else null
    }