package steuerung.gamepad

import com.soywiz.korev.*
import com.soywiz.korma.geom.*
import eventController.Event

class GamepadButtonEvent(val button : GameButton,val down : Boolean,val index:Int): Event {
}
class GamepadStickEvent(val point: Point,val stick: GameStick,val index:Int):Event {
}