/**
 * This class holds the actual states of an object [owner]
 */
class StateExecutor<T>(private val owner: T, val innerState: State<T>) {

    //add code to the onBegin lambda of [State]
    fun onBegin(body: T.() -> Unit) {
        innerState.begin = body
    }

    //add code to the onExecute lambda of [State]
    fun onExecute(body: T.() -> Unit) {
        innerState.execute = body
    }

    //add code to the onEnd lambda of [State]
    fun onEnd(body: T.() -> Unit) {
        innerState.end = body
    }

    //calls the begin function
    fun callBegin() {
        innerState.doBegin(this.owner)
    }

    //calls the execute function
    fun callExecute() {
        innerState.doExecute(this.owner)
    }

    //calls the end function
    fun callEnd() {
        innerState.doEnd(this.owner)
    }

    //random crap, not important

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != StateExecutor::class) return false
        val obj = other as StateExecutor<*>

        return this.innerState == other.innerState && this.owner == other.owner
    }

    override fun hashCode(): Int {
        var result = owner?.hashCode() ?: 0
        result = 31 * result + innerState.hashCode()
        return result
    }

}
