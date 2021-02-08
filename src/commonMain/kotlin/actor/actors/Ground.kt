package actor.actors

import actor.Actor
import com.soywiz.korge.view.Container
import physic.Physics

class Ground(parent: Container): Actor(parent) {

    //override val physics: Physics = Physics(this)

    override fun onCreate() {
        initPhysics(false)
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