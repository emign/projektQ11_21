package actor

import com.soywiz.korge.view.Container
import com.soywiz.korma.geom.Point
import fsm.StateUser

/**
 * An actor is basically an object which you can see on screen (Player, Enemy, Ground, ...)
 */

abstract class Actor() : Container() {

    var position: Point = Point(this.y, this.y)
    var lastPosition: Point = position
    var newScale: Double = this.scale

    var timer: Int = 0
    var dead: Boolean = false

    abstract fun onCreate()
    abstract fun onExecute(dt: Double)
    abstract fun onDelete()

    abstract fun updateGraphics()

    abstract fun kill()
}