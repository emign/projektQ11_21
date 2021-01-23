package eventBus

import com.soywiz.korev.*
import com.soywiz.korge.component.*
import com.soywiz.korge.view.*

class Input(override val view: View, private val bus : EventBus) : KeyComponent {

    private var onGround = true

    var rightKey = Key.RIGHT
    var leftKey = Key.LEFT
    var jumpKey = Key.SPACE

    var preve : KeyEvent? = null

    init {
        bus.register<GroundedEvent>{ground()}
    }

    private fun ground(){
        onGround=true
    }

    override fun Views.onKeyEvent(event: KeyEvent) {

        val keys = input.keys

        if (keys.justPressed(leftKey)&&!keys[rightKey])bus.send(LeftEvent())
        if (keys.justPressed(rightKey)&&!keys[leftKey]){bus.send(RightEvent())}
        if (keys.justPressed(jumpKey)&&onGround){bus.send(JumpEvent());onGround=false}
        if (keys.justPressed(rightKey)&&keys[leftKey])bus.send(LeftEvent())
        if (keys.justPressed(leftKey)&&keys[rightKey])bus.send(RightEvent())
        else if (keys.justReleased(rightKey)||keys.justReleased(leftKey))bus.send(IdleEvent())

    }
}