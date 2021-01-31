package physic

import actor.Actor
import actor.MovingActor
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.View
import com.soywiz.korge.view.addUpdater

class AABB(val owner: SolidRect, var left: Double = owner.x, var right: Double = owner.x + owner.width, var top: Double = owner.y, var bottom: Double = owner.y + owner.height) {

    init {
        owner.addUpdater {
            this@AABB.left = this.x
            this@AABB.right = this.x + this.width
            this@AABB.top = this.y
            this@AABB.bottom = this.y + this.height
        }
    }
}