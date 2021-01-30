package fsm

interface StateUser {
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

inline fun <reified T: StateUser> T.declareState(noinline onBegin: () -> Unit = {}, noinline onExecute: () -> Unit = {}, noinline onEnd: () -> Unit = {}): StateExecutor {
    val currentManager = StateManager.stateMachines[T::class]
    if (currentManager != null) {
        return currentManager.createState(onBegin, onExecute, onEnd)
    }
    error("You have to declare a manager first!")
}

inline fun <reified T: StateUser> T.useStates(callback: StateManager.() -> Unit = {}): StateManager {
    val manager = StateManager()
    StateManager.stateMachines[T::class] = manager
    manager.apply { callback() }
    return manager
}