package multiplayer

import com.soywiz.korio.async.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

actual class Client actual constructor(private val ip:String, private val port:Int, private val scope:CoroutineScope): ClientServer() {

    private lateinit var out : SendChannel<Frame>

    private lateinit var callback: (String) -> Unit

    private var running = true

    actual override fun create() {
        val client = HttpClient(CIO)
        scope.launchImmediately {
            client.ws(
                port = port,
                host = ip,
                path = "/"
            ){
                out=outgoing
                try {
                    incoming.consumeEach { frame ->
                        when (frame) {
                            is Frame.Text -> callback(frame.readText())
                        }
                    }
                }finally {

                }
            }
        }
    }

    actual override fun send(data: MultiplayerData) {
        scope.launchImmediately {
            out.send(Frame.Text(data.toString()))
        }
    }

    actual override fun setGet(callback: (String) -> Unit) {
        this.callback =callback
    }


}