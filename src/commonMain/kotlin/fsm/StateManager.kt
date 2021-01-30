package fsm

import kotlin.reflect.KClass

/**
 * Create a new [StateMachine] and optionally apply a callback
 */

class StateManager {

    companion object {
        val stateMachines: MutableMap<KClass<*>, StateManager> = mutableMapOf()
    }

    //list of all states of the class
    private var states = mutableMapOf<Int, StateExecutor>()

    //the current state the
    private var currentState: StateExecutor = StateExecutor()

    //all finished states are stored here to maybe go to the previous state if necessary -> not used yet
    val stateStack = mutableListOf<StateExecutor>()

    //changes the current state
    fun doStateChange(new: StateExecutor) {
        currentState.callEnd()
        this.stateStack.add(stateStack.size, currentState)
        new.callBegin()
        currentState = new
    }

    //sets up the starting state
    fun setStartState(state: StateExecutor) = doStateChange(state)

    //creates a new fsm.old.State for the object called "owner" -> the state only affects him
    fun createState(onBegin: () -> Unit = {}, onExecute: () -> Unit = {}, onEnd: () -> Unit = {}): StateExecutor {
        val stateBase = State().apply {
            begin = onBegin
            execute = onExecute
            end = onEnd
        }
        val state = StateExecutor(stateBase)
        this.states[stateBase.id] = state
        return state
    }

    //updates the current state
    fun updateCurrentState() {
        currentState.callExecute()
    }

    fun getCurrentState(): StateExecutor {
        return currentState
    }
}
