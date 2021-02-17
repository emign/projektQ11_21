package actor.actors

import actor.Actor
import com.soywiz.korge.view.Container

class Platform(parent: Container): Actor(parent) {

    //override val physic.getPhysics: Physics = Physics(this)

    override fun onCreate() {
        initPhysics(false)
    }

    override fun onExecute(dt: Double) {
        //physic.getPhysics.update(dt)
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