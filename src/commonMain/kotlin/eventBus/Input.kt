package eventBus

import com.soywiz.korev.*
import com.soywiz.korge.baseview.*
import com.soywiz.korge.component.*
import com.soywiz.korge.view.*

class Input(override val view: BaseView, private val bus : EventBus) : KeyComponent {

    private var onGround = true

    var rightKeys : List<Key> = listOf(Key.RIGHT)
    var leftKeys : List<Key> = listOf(Key.LEFT)
    var jumpKeys : List<Key> = listOf(Key.SPACE)

    init {
        bus.register<GroundedEvent>{ground()}
    }

    private fun ground(){
        onGround=true
    }

    override fun Views.onKeyEvent(event: KeyEvent) {
        var rightOr : Boolean = false
        rightKeys.forEach { rightOr=rightOr||keys[it] }

        var leftOr : Boolean = false
        leftKeys.forEach { leftOr=leftOr||keys[it] }

        if (leftOr&&!rightOr)bus.send(LeftEvent())
        if (rightOr&&!leftOr)bus.send(RightEvent())
        if (!rightOr&&!leftOr)bus.send(IdleEvent())
        if (rightOr&&leftOr)bus.send(IdleEvent())

        var jumpOr = false
        jumpKeys.forEach { jumpOr=jumpOr||keys[it] }

        if (jumpOr&&onGround){bus.send(JumpEvent());onGround=false}
    }
}