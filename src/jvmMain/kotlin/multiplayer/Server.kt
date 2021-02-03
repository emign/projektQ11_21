package multiplayer

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


actual class Server actual constructor(private val ip:String, private val port:Int, private val scope: CoroutineScope) : ClientServer() {

    private lateinit var out : SendChannel<Frame>

    private lateinit var callback: (String) -> Unit

    private var running = true

    actual override fun create() {
        embeddedServer(CIO,port,ip){
            install(WebSockets) {
            }
            install(DefaultHeaders)
            install(CallLogging)
            routing {
                webSocket ("/") {
                    out=outgoing
                    try {
                        incoming.consumeEach { frame ->
                            when (frame){
                                is Frame.Text->callback(frame.readText())
                            }
                        }
                    }finally {
                        
                    }
                }
            }
        }
    }

    actual override fun setGet(callback: (String) -> Unit) {
    }

    actual override fun send(data: MultiplayerData) {
    }
}