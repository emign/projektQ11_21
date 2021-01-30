package fsm

/**
 * This class holds the actual states of an object [owner]
 */

class StateExecutor(val state: State = State()) {

    //add code to the onBegin lambda of [fsm.old.State]
    fun onBegin(body: () -> Unit) {
        state.begin = body
    }

    //add code to the onExecute lambda of [fsm.old.State]
    fun onExecute(body: () -> Unit) {
        state.execute = body
    }

    //add code to the onEnd lambda of [fsm.old.State]
    fun onEnd(body: () -> Unit) {
        state.end = body
    }

    //calls the begin function
    fun callBegin() {
        state.begin()
    }

    //calls the execute function
    fun callExecute() {
        state.execute()
    }

    //calls the end function
    fun callEnd() {
        state.end()
    }

    //random crap, not important

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != StateExecutor::class) return false
        val obj = other as StateExecutor

        return this.state == other.state
    }
}
