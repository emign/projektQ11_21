package multiplayer

import com.soywiz.korio.async.*
import com.soywiz.korio.net.*
import com.soywiz.korio.stream.*
import kotlinx.coroutines.*

class Client(val port : Int, val host : String, private val scope : CoroutineScope) {

    private lateinit var client : AsyncClient
    lateinit var inputStream : AsyncInputStream
    lateinit var outputStream : AsyncOutputStream

    init {
        scope.launchImmediately {
            client= createTcpClient(host, port)
            inputStream=client
            outputStream=client
        }
    }

}