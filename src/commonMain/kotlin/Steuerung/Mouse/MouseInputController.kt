package Steuerung.Mouse
import Steuerung.Mouse.MouseEvent as SteuerungMouseEvent
import com.soywiz.korev.MouseButton
import com.soywiz.korev.MouseEvent
import com.soywiz.korge.baseview.BaseView
import com.soywiz.korge.component.MouseComponent
import com.soywiz.korge.view.Views
import eventController.eventController

class MouseInputController(override val view: BaseView) : MouseComponent {
   var mouseLeftState: Boolean=false
   var mouseRightState: Boolean=false
    var mouseX: Int =0
    var mouseY: Int= 0
    override fun onMouseEvent(views: Views, event: MouseEvent) {
        val prev = MouseData(mouseX, mouseY, mouseLeftState, mouseRightState)

            when (event.type) {
                MouseEvent.Type.MOVE -> {
                    mouseX= event.x
                    mouseY= event.y
                }
                MouseEvent.Type.DOWN -> {
                    if (event.button== MouseButton.RIGHT) mouseRightState=true
                    if ( event.button==MouseButton.LEFT) mouseLeftState=true
                }
                MouseEvent.Type.UP -> {
                    if (event.button== MouseButton.RIGHT) mouseRightState=false
                    if ( event.button==MouseButton.LEFT) mouseLeftState=false
                }
            }
        val act = MouseData(mouseX, mouseY, mouseLeftState, mouseRightState)
        if (!act.equalsData(prev)) eventController.send(SteuerungMouseEvent(act))
    }

}