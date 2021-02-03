import com.soywiz.korio.async.launch
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.SendChannel

class Client(private val port:Int, host:String):ClientServer() {
    var client : HttpClient = HttpClient(CIO){
        install(WebSockets)
    }
    var running = true
    lateinit var write : SendChannel<Frame>

    init {
        launch(Dispatchers.Default){
            client.ws(
                method = HttpMethod.Get,
                port = port,
                host = host,
                path = "/"
            ){
                write = outgoing
                launch {
                    while (running){
                        for (frame in incoming){
                            when(frame){
                                is Frame.Text ->{
                                    MultiplayerMain.receive(frame.readText())
                                }
                            }
                        }
                    }
                }

            }
        }
    }
    override fun stop(){
        try {
            client.close()
        }catch (e:Throwable){
            client.cancel()
        }
    }

    override suspend fun send(str:String) {
        write.send(Frame.Text(str))
    }
}