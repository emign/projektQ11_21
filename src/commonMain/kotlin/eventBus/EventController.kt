package eventBus

import com.soywiz.korge.bus.*
import com.soywiz.korio.async.*
import kotlinx.coroutines.*
import kotlin.reflect.*

class EventController(private val scope: CoroutineScope) {

    private val bus = GlobalBus()

    /**
     *Sends an Event through the EventController with the [Event] as Parameter
     */
    fun send(message: Event) {
        scope.launchImmediately {
            bus.send(message)
        }
    }

    /**
     * Registers a [handler] (lambda) for a specific [Event]
     */
    fun <T : Event> register(clazz: KClass<out T>, handler: suspend (T) -> Unit) {
        bus.register(clazz, handler)
    }

    inline fun <reified T : Event> register(noinline handler: suspend (T) -> Unit) {
        register(T::class, handler)
    }

}