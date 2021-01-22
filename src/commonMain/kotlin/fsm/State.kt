import kotlin.reflect.KClass

/**
 * This class holds the actual states of an object [owner]
 */

class State<T>(private val owner: T, val codeHandler: StateCodeHandler<T>) {

    //add code to the onBegin lambda of [State]
    fun onBegin(body: T.() -> Unit) {
        codeHandler.begin = body
    }

    //add code to the onExecute lambda of [State]
    fun onExecute(body: T.() -> Unit) {
        codeHandler.execute = body
    }

    //add code to the onEnd lambda of [State]
    fun onEnd(body: T.() -> Unit) {
        codeHandler.end = body
    }

    //calls the begin function
    fun callBegin() {
        codeHandler.begin(owner)
    }

    //calls the execute function
    fun callExecute() {
        codeHandler.execute(owner)
    }

    //calls the end function
    fun callEnd() {
        codeHandler.end(owner)
    }

    //random crap, not important

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != State::class) return false
        val obj = other as State<*>

        return this.codeHandler == other.codeHandler && this.owner == other.owner
    }

    override fun hashCode(): Int {
        var result = owner?.hashCode() ?: 0
        result = 31 * result + codeHandler.hashCode()
        return result
    }
}
