package fsm

import kotlin.reflect.KClass

/**
 * Handles multiple [StateExecutor]s and calls actions. All created [StateExecutor]s are stored here
 * This class executes the actual code contained in each state
 */

class StateManager {

    companion object {
        val stateMachines: MutableMap<KClass<*>, StateManager> = mutableMapOf()
    }

    //list of all states of the class
    private var states = mutableMapOf<Int, StateExecutor>()

    private var active: Boolean = false

    //the current state the
    private var currentState: StateExecutor = StateExecutor()

    //all finished states are stored here to maybe go to the previous state if necessary -> not used yet
    val stateStack = mutableListOf<StateExecutor>()

    //changes the current state
    fun doStateChange(new: StateExecutor) {
        active = false
        currentState.callEnd()
        this.stateStack.add(stateStack.size, currentState)
        new.callBegin()
        currentState = new
        active = true
    }

    //sets up the starting state
    fun setStartState(state: StateExecutor) {
        active = false
        state.callBegin()
        currentState = state
        active = true
    }

    //creates a new State and adds it to the Manager
    fun createState(onBegin: () -> Unit = {}, onExecute: (dt: Double) -> Unit = {}, onEnd: () -> Unit = {}): StateExecutor {
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
    fun updateCurrentState(dt: Double) {
        if (active) {
            currentState.callExecute(dt)
        }
    }

    fun getCurrentState(): StateExecutor {
        return currentState
    }
}
