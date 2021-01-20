import fsm.StateMachine

/**
*  base class for a data structure base. This is only used internal, normal states are [StateExecutor]
 */
class State<T>(val belongingMachine: StateMachine<T>) {

    companion object {
        var id: Int = 0
    }

    //id to identify a state
    val id: Int = Companion.id

    init {
        Companion.id += 1
    }

    //the actions the state contains
    var begin: T.() -> Unit = {}
    var execute: T.() -> Unit = {}
    var end: T.() -> Unit = {}

    //every action needs an owner to which the action should be performed
    fun doBegin(owner: T) {
        begin(owner)
    }

    fun doExecute(owner: T) {
        execute(owner)
    }

    fun doEnd(owner: T) {
        end(owner)
    }

    //extra stuff, not important
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != State::class) return false
        val obj = other as State<*>

        return this.id == other.id && this.belongingMachine == other.belongingMachine
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + belongingMachine.hashCode()
        result = 31 * result + begin.hashCode()
        result = 31 * result + execute.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

}