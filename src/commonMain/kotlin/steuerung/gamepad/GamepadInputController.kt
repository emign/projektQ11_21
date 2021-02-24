package steuerung.gamepad

import steuerung.*
import com.soywiz.korev.*
import com.soywiz.korge.component.*
import com.soywiz.korge.view.*
import eventController.*

class GamepadInputController(override val view: View):GamepadComponent {
    override fun onGamepadEvent(views: Views, event: GamePadConnectionEvent) {
        eventController.send(GamepadConnectEvent(event.gamepad))
    }

    override fun onGamepadEvent(views: Views, event: GamePadUpdateEvent) {
        event.gamepads.forEachIndexed { index, gamepadInfo ->
            if (InputController.trackedGamePads.contains(index)){
                InputController.trackedButtons.forEach { gameButton ->
                    eventController.send(GamepadButtonEvent(gameButton,gamepadInfo[gameButton]>0.0,gamepadInfo.index))
                }
                InputController.trackedSticks.forEach { gameStick ->
                    eventController.send(GamepadStickEvent(gamepadInfo[gameStick].normalized,gameStick,gamepadInfo.index))
                }
            }
        }
    }



}