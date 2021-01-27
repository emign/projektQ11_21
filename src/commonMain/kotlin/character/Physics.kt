package character

import character.*
import com.soywiz.klock.*
import com.soywiz.korge.box2d.*
import com.soywiz.korge.component.*
import eventBus.*
import org.jbox2d.common.*
import org.jbox2d.dynamics.*

/**
 * Update position based on velocity and check for collision
 */
class Physics(override val view: CharacterBase, val bus: EventBus, val world: World) : UpdateComponent {

    init {
        view.body?.isFixedRotation = true
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
