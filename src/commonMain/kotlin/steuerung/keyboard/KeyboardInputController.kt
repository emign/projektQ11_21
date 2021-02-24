package steuerung.keyboard


import steuerung.*
import com.soywiz.korev.KeyEvent
import com.soywiz.korge.component.KeyComponent
import com.soywiz.korge.view.View
import com.soywiz.korge.view.Views
import eventController.eventController

class KeyboardInputController(override val view: View) : KeyComponent {
    override fun Views.onKeyEvent(event: KeyEvent) {
        if (event.typeDown&&InputController.trackedKeys.contains(event.key)){
            eventController.send(KeyEvent(event.key,true))
        }
        else if (InputController.trackedKeys.contains(event.key)){
            eventController.send(KeyEvent(event.key,false))
        }
    }
}