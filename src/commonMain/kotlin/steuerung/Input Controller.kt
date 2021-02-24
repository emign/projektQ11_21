package steuerung

import steuerung.gamepad.*
import steuerung.keyboard.*
import steuerung.mouse.*
import com.soywiz.korev.*
import com.soywiz.korge.view.*

object InputController {

    var trackedKeys = emptyList<Key>().toMutableList()

    fun View.initKeyboardController(tracked : List<Key>?){
        tracked?.onEach { trackedKeys.add(it) }
        this.addComponent(KeyboardInputController(this))
    }

    fun View.initMouseController(){
        this.addComponent(MouseInputController(this))
    }

    var trackedGamePads = emptyList<Int>().toMutableList()
    var trackedSticks = emptyList<GameStick>().toMutableList()
    var trackedButtons = emptyList<GameButton>().toMutableList()

    fun View.initGamepadController(gamePads : List<Int>?,sticks : List<GameStick>?,buttons : List<GameButton>?){
        gamePads?.forEach { trackedGamePads.add(it) }
        sticks?.forEach { trackedSticks.add(it) }
        buttons?.forEach { trackedButtons.add(it) }
        this.addComponent(GamepadInputController(this))
    }
}