package physic

import com.soywiz.kds.getExtra
import com.soywiz.kds.setExtra
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.addUpdater
import org.jbox2d.common.Vec2

/**
 * Attach a [Listener] to a [Container]. The listener will automatically be updated every frame with the correct deltaTime
 * and updates all physics contained in it. To add a physics object to a [Container], see [addPhysicsComponent]
 * @param gravity The gravity for the whole physics system as [Vec2]. By default it is Vec2(0f, 9.81f)
 */
fun Container.usePhysics(gravity: Vec2 = Vec2(0f, 9.81f)) {
    val listener = Listener(gravity)
    this.setExtra("physicsListener", listener)
    this.addUpdater {
        listener.update(it.milliseconds.toFloat())
    }
}

/**
 * Adds a new [Physics]-object to the listener of the container.
 * Be sure to first initialize a [Listener] to the Container with [Container.usePhysics], otherwise nothing will be updated
 * @param owner The [SolidRect] to which the physics-component is added. Should be a child of the [Container], because the listener of the container updates only all its children
 * @param friction The friction vector (used for air resistance). By default it's Vec2(2.0f, 0.5f)
 * @param isDynamic Should the [Physics]-object move or be static? Specify it here!
 * @param coefficient The coefficient for calculating the pixel distance from meters. The higher you set it, the faster the objects will move. By default 120.0
 * @param collisionCallback A custom callback which is executed when a collision with [owner] occurs. It takes another [Physics]-object as parameter for the collision partner
 */
fun Container.addPhysicsComponent(
    owner: SolidRect,
    friction: Vec2 = Vec2(2.0f, 0.5f),
    isDynamic: Boolean = true,
    coefficient: Vec2 = Vec2(120.0f, 120.0f),
    collisionCallback: Physics.(Physics) -> Unit = {}
) {
    val listener = this.getExtra("physicsListener")
    if (listener == null || listener::class != Listener::class) {
        println("ERROR: Can't add a physics component to this view because the Listener is not implemented. You can do this with Stage.usePhysics()! ")
    } else {
        val stageListener = listener as Listener
        val physics = Physics(owner, friction, isDynamic, coefficient, collisionCallback)
        owner.setExtra("physics", physics)
        stageListener.addPhysics(physics)
    }
}

/**
 * Adds a couple of [Physics]-objects to a [Container] and its [Listener] (create it with [Container.usePhysics].
 * This method works exactly like [Container.addPhysicsComponent], but here you can specify values such as friction,
 * isDynamic or the collisionCallback just once, and the values will be taken for all objects passed in in [owners]
 */

fun Container.addPhysicsComponentsTo(
    vararg owners: SolidRect,
    friction: Vec2 = Vec2(2.0f, 0.5f),
    isDynamic: Boolean = true,
    coefficient: Vec2 = Vec2(120.0f, 120.0f),
    collisionCallback: Physics.(Physics) -> Unit = {}
) {
    owners.forEach {
        this.addPhysicsComponent(it, friction, isDynamic, coefficient, collisionCallback)
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