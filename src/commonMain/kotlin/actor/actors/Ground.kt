package actor.actors

import actor.Actor
import physic.Physics

class Ground: Actor() {

    //override val physics: Physics = Physics(this)

    override fun onCreate() {
        initPhysics()
    }

    override fun onExecute(dt: Double) {
        //physics.update(dt)
    }

    override fun onDelete() {
        TODO("Not yet implemented")
    }

    override fun updateGraphics() {
        TODO("Not yet implemented")
    }

    override fun kill() {
        TODO("Not yet implemented")
    }
}