package character.physic

import character.CharacterBase
import com.soywiz.kds.getExtra
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.box2dWorldComponent
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korge.view.onCollision
import com.soywiz.korge.view.onCollisionShape
import eventBus.EventBus
import eventBus.PlayerCollision
import eventBus.SpriteCollision
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

/**
 * Update position based on velocity and check for collision
 */
class Physics(override val view: CharacterBase, val bus: EventBus) : UpdateComponent {

    init {
        view.registerBodyWithFixture(type = BodyType.DYNAMIC, density = 1.0, friction = 0.2)
        view.body?.isFixedRotation = true
        registerOnCollision()
    }

    private fun registerOnCollision() {
        view.onCollision({ it.getExtra("type") == Hit_Type.Player }) { col ->
            //On Collision with Player
            //determine on which object the collision occured TODO
            bus.send(PlayerCollision())
        }
        view.onCollision({ it.getExtra("type") == Hit_Type.Enemy }) { col ->
            //On Collision with Enemy
            //determine on which object the collision occured TODO
            bus.send(SpriteCollision())
        }
    }

    var isActive: Boolean = false

    override fun update(dt: TimeSpan) {


        when (view.stateManager.getCurrentState()) {

            view.jumpState -> {
                view.body?.applyLinearImpulse(
                    Vec2(0f, -view.characterXmlData.jumpHeight.toFloat()),
                    Vec2(0.0f, 0.0f), true
                )
            }
            view.walkLeftState -> {
                view.body?.linearVelocityX = -view.characterXmlData.movementSpeed.toFloat()
            }
            view.walkRightState -> {
                view.body?.linearVelocityX = view.characterXmlData.movementSpeed.toFloat()
            }

        }

    }

}
