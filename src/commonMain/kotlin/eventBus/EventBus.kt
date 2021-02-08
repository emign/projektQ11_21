package eventBus

import com.soywiz.korge.bus.*
import com.soywiz.korio.async.*
import kotlinx.coroutines.*
import kotlin.reflect.*

class EventBus(val scope: CoroutineScope) {

    private val bus = GlobalBus()

    fun send(message: Any) {
        scope.launchImmediately {
            bus.send(message)
        }
    }

    fun <T : Any> register(clazz: KClass<out T>, handler: suspend (T) -> Unit) {
        bus.register(clazz, handler)
    }

    inline fun <reified T : Any> register(noinline handler: suspend (T) -> Unit) {
        register(T::class, handler)
    }

}