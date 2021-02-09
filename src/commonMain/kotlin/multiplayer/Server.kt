package multiplayer

import com.soywiz.korio.async.*
import com.soywiz.korio.net.*
import com.soywiz.korio.stream.*
import kotlinx.coroutines.*

class Server(val port : Int, val host : String,private val scope : CoroutineScope) {

    private lateinit var server : AsyncServer
    var connections : MutableList<Pair<AsyncInputStream,AsyncOutputStream>> = emptyList<Pair<AsyncInputStream,AsyncOutputStream>>().toMutableList()
    init {
        launchImmediately(scope.coroutineContext){
            server = createTcpServer(port, host)
            server.listen { connections.add(Pair(it,it)) }
        }
    }

}