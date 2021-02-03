import com.soywiz.korio.async.launch
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.SendChannel
import java.util.concurrent.TimeUnit

class Server(private val port:Int, host:String):ClientServer() {
    private var server:ApplicationEngine
    private var running = true
    private lateinit var write : SendChannel<Frame>
    init {
        server= embeddedServer(CIO,port=port,host=host){
            routing {
                webSocket ("/"){
                    write = outgoing
                    launch {
                        while (running){
                            for (frame in incoming){
                                when (frame){
                                    is Frame.Text->{
                                        var data = frame.readText()
                                        MultiplayerMain.receive(data)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.start()
    }

    override suspend fun send(str:String) {
        write.send(Frame.Text(str))
    }

    override fun stop() {
        try {
            running=false
            server.stop(1,1,TimeUnit.SECONDS)
        }
        catch (e:Exception){
            println("Could not stop server")
            println(e.stackTrace)
        }
    }
}