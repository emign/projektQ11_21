package fsm

/**
 * Every class who can use [State]s has to implement this interface. It's basically just a wrapper class for
 * a [StateManager]
 */
interface StateUser {
    val manager: StateManager

    fun setStartState(state: StateExecutor) {
        manager.setStartState(state)
    }

    fun doStateChange(state: StateExecutor) {
        manager.doStateChange(state)
    }

    fun updateCurrentState(dt: Double) {
        manager.updateCurrentState(dt)
    }

    fun getCurrentState(): StateExecutor {
        return manager.getCurrentState()
    }
}