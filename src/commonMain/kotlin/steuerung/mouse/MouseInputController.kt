package steuerung.mouse
import com.soywiz.korev.*
import com.soywiz.korev.MouseEvent
import com.soywiz.korge.component.*
import com.soywiz.korge.view.*
import eventController.*
import steuerung.mouse.MouseEvent as SteuerungMouseEvent

class MouseInputController(override val view: View) : MouseComponent {
   var mouseLeftState: Boolean=false
   var mouseRightState: Boolean=false
    var mouseX: Int =0
    var mouseY: Int= 0
    override fun onMouseEvent(views: Views, event: MouseEvent) {
        val prev = MouseData(mouseX, mouseY, mouseLeftState, mouseRightState)

            when (event.type) {
                MouseEvent.Type.MOVE -> {
                    mouseX= view.globalToLocalXY(event.x.toDouble(), event.y.toDouble()).x.toInt()
                    mouseY= view.globalToLocalXY(event.x.toDouble(), event.y.toDouble()).y.toInt()
                }
                MouseEvent.Type.DOWN -> {
                    if (event.button== MouseButton.RIGHT) mouseRightState=true
                    if ( event.button==MouseButton.LEFT) mouseLeftState=true
                }
                MouseEvent.Type.UP -> {
                    if (event.button== MouseButton.RIGHT) mouseRightState=false
                    if ( event.button==MouseButton.LEFT) mouseLeftState=false
                }
                else -> {

                }
            }
        val act = MouseData(mouseX, mouseY, mouseLeftState, mouseRightState)
        if (!act.equalsData(prev)) eventController.send(SteuerungMouseEvent(act))
    }

}