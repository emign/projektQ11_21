package eventController

import com.soywiz.korge.bus.*
import com.soywiz.korio.async.*
import kotlinx.coroutines.*
import kotlin.reflect.*

/**
 * the Object EventController handles Events globally
 */
object eventController {

    private val bus = GlobalBus()

    val scope = Dispatchers.Default

    /**
     * Sends an Event through the EventController with the [Event] as Parameter
     * @param message is the [Event] that needs to be sent
     */
    fun send(message: Event) {
        launchImmediately(scope) {
            bus.send(message)
        }
    }

    /**
     * Registers a [handler] (lambda) for a specific [Event]
     * @param handler is the callback lambda
     */
    fun <T : Event> register(clazz: KClass<out T>, handler: suspend (T) -> Unit) {
        bus.register(clazz, handler)
    }

    inline fun <reified T : Event> register(noinline handler: suspend (T) -> Unit) {
        register(T::class, handler)
    }

}