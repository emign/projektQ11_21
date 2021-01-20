package fsm

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors


//just a basic class for testing states. Can be removed later on
class Entity {

    val view = SolidRect(100.0, 50.0, Colors.YELLOW).apply { xy(300, 300) }

    //creating the StateMachine, the basis for creating and managing states
    val stateSystem = createStateBase()

    // creating some example states
    val walkState = stateSystem.createState(this)
    val jumpState = stateSystem.createState(this)
    val idleState = stateSystem.createState(this)

    init {

        //initialize the Entity
        onCreate()

        //initialize the states and add actions to them
        initStates()

        view.addUpdater {
            //update the Entity
            onExecute()
        }

        //KeyListerner for changing states
        view.keys.down {
            when(it.key) {
                Key.I -> stateSystem.doStateChange(idleState)
                Key.W -> stateSystem.doStateChange(walkState)
                Key.J -> stateSystem.doStateChange(jumpState)
            }
        }
    }


    fun onCreate() {
        //setting first state
        stateSystem.setStartState(idleState)
    }

    //add onBegin, onExecute and onEnd Actions to the states. These will be executed when the state is active
    fun initStates() {
        walkState.onBegin {
            view.color = Colors.GREEN
        }

        walkState.onExecute {
            view.x += 1
        }

        walkState.onEnd {
            view.color = Colors.YELLOW
        }

        jumpState.onBegin {
            view.color = Colors.RED
        }

        jumpState.onExecute {
            view.y -= 1
        }

        jumpState.onEnd {
            view.color = Colors.YELLOW
            view.y += 100
        }

        idleState.onBegin {
            view.color = Colors.BLUE
        }

        idleState.onExecute {
            if (view.alpha > 0.0) view.alpha -= 0.005
            else view.alpha = 1.0
        }

        idleState.onEnd {
            view.color = Colors.YELLOW
        }
    }

    //update the state system -> updates current state
    fun onExecute() {
        stateSystem.update()
    }

}