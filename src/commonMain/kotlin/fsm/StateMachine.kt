package fsm

import State
import StateExecutor

/**
 * Create a new [StateMachine] and optionally apply a callback
 */

inline fun <T/*:fsm.Entity or whatever the base type is*/> T.createStateBase(callback: T.() -> Unit = {}): StateMachine<T> {
    return StateMachine<T>().apply { callback() }
}

/**
 * This class manages all the states of a class-object(for example [Entity]).
 */
class StateMachine<T> {

    //the current state the
    private lateinit var currentState: StateExecutor<T>

    //list of all states of the class
    private var states = mutableMapOf<Int, State<T>>()
    //all finished states are stored here to maybe go to the previous state if necessary -> not used yet
    private var stateStack = mutableListOf<State<T>>()

    //changes the current state
    fun doStateChange(new: StateExecutor<T>) {
        if(::currentState.isInitialized) {
            currentState.callEnd()
            this.stateStack.add(currentState.innerState)
        }
        currentState = new
        currentState.callBegin()
    }

    //sets up the starting state
    fun setStartState(state: StateExecutor<T>) {
        currentState = state
    }

    //creates a new State for the object called "owner" -> the state only affects him
    fun createState(owner: T): StateExecutor<T> {
        val state = State<T>(this)
        state.belongingMachine.states[state.id] = state
        return StateExecutor(owner, state)
    }

    //updates the current state
    fun update() {
        currentState.callExecute()
    }
}
