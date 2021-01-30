package fsm

import kotlin.reflect.KClass

/**
 * Create a new [StateMachine] and optionally apply a callback
 */

interface Stateable {
    val manager: StateManager

    fun setStartState(state: StateExecutor) {
        manager.setStartState(state)
    }

    fun doStateChange(state: StateExecutor) {
        manager.doStateChange(state)
    }

    fun updateCurrentState() {
        manager.updateCurrentState()
    }

    fun getCurrentState(): StateExecutor {
        return manager.getCurrentState()
    }
}

inline fun <reified T: Stateable> T.declareState(): StateExecutor {
    val currentManager = StateManager.stateMachines[T::class]
    if (currentManager != null) {
        return currentManager.createState()
    }
    error("You have to declare a manager first!")
}

inline fun <reified T: Stateable> T.useStates(callback: StateManager.() -> Unit = {}): StateManager {
    val manager = StateManager()
    StateManager.stateMachines[T::class] = manager
    manager.apply { callback() }
    return manager
}

/**
 * This class manages all the states of a class-object(for example [Entity]).
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
    fun createState(): StateExecutor {
        val stateBase = State()
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
