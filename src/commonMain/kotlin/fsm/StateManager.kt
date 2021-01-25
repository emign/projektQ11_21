package fsm

import State
import StateCodeHandler
import character.CharacterBase
import com.soywiz.korio.lang.currentThreadId

/**
 * Create a new [StateMachine] and optionally apply a callback
 */

inline fun <T/*:fsm.Entity or whatever the base type is*/> T.createStateManager(callback: StateManager<T>.() -> Unit = {}): StateManager<T> {
    return StateManager<T>().apply { callback() }
}

fun <T/*: Whatever the base type for having states is*/> T.createState(stateManager: StateManager<T>): State<T> {
    return stateManager.createState(this)
}

/**
 * This class manages all the states of a class-object(for example [Entity]).
 */
class StateManager<T> {

    //list of all states of the class
    private var states = mutableMapOf<Int, State<T>>()

    //the current state the
    private lateinit var currentState: State<T>

    //all finished states are stored here to maybe go to the previous state if necessary -> not used yet
    val stateStack = mutableListOf<State<T>>()

    //changes the current state
    fun doStateChange(new: State<T>) {
        if(::currentState.isInitialized) {
            currentState.callEnd()
            this.stateStack.add(stateStack.size, currentState)
        }
        currentState = new
        currentState.callBegin()
    }

    //sets up the starting state
    fun setStartState(state: State<T>) = doStateChange(state)

    //creates a new State for the object called "owner" -> the state only affects him
    fun createState(owner: T): State<T> {
        val stateBase = StateCodeHandler<T>()
        val state = State(owner, stateBase)
        this.states[stateBase.id] = state
        return state
    }

    //updates the current state
    fun updateCurrentState() {
        currentState.callExecute()
    }

    fun getCurrentState(): State<T> {
        if(::currentState.isInitialized) return currentState else error("currentState is null")
    }
}
