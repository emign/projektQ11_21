/**
 *  base class for a data structure base. This is only used internal, normal states are [State]
 */

class StateCodeHandler<T> {

    companion object {
        var id: Int = 0
    }

    val id: Int = Companion.id

    init {
        Companion.id += 1
    }

    //the actions the state contains
    var begin: T.() -> Unit = {}
    var execute: T.() -> Unit = {}
    var end: T.() -> Unit = {}

    //extra stuff, not important
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != StateCodeHandler::class) return false
        val obj = other as StateCodeHandler<*>

        return this.begin == other.begin && this.execute == other.execute && this.end == other.end && this.id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + begin.hashCode()
        result = 31 * result + execute.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

}