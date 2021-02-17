package Steuerung.Keyboard


import com.soywiz.korev.Key
import com.soywiz.korev.KeyEvent
import com.soywiz.korge.component.KeyComponent
import com.soywiz.korge.view.View
import com.soywiz.korge.view.Views
import eventController.eventController

class KeyboardInputController(override val view: View) : KeyComponent {

    var jumpKey: Key = Key.SPACE
    var leftKey: Key=Key.A
    var rightKey: Key = Key.D
    var blockKey: Key= Key.S
    var attackKey: Key= Key.K
    var superAttackKey:Key= Key.O
    var rangedAttackKey: Key= Key.I

    var jumpState: Boolean= false
    var leftState: Boolean= false
    var rightState: Boolean= false
    var blockState: Boolean= false
    var attackState: Boolean= false
    var superAttackState:Boolean= false
    var rangedAttackState: Boolean= false

    override fun Views.onKeyEvent(event: KeyEvent) {
        val prev = KeyData(jumpState,leftState,rightState,attackState,superAttackState,rangedAttackState,blockState)
        if (event.typeDown){
            when (event.key) {
                jumpKey -> jumpState=true
                leftKey -> leftState= true
                rightKey -> rightState=true
                blockKey -> blockState= true
                attackKey -> attackState = true
                superAttackKey -> superAttackState = true
                rangedAttackKey -> rangedAttackState= true
            }

        }
        else{
            when (event.key) {
                jumpKey -> jumpState = false
                leftKey -> leftState = false
                rightKey -> rightState = false
                blockKey -> blockState = false
                attackKey -> attackState = false
                superAttackKey -> superAttackState = false
                rangedAttackKey -> rangedAttackState = false
            }
        }
        val act = KeyData(jumpState,leftState,rightState,attackState,superAttackState,rangedAttackState,blockState)
        if (!act.equalsData(prev))eventController.send(KeyEvent(act))
    }
}