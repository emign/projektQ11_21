package multiplayer

import com.soywiz.korio.async.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*

@InternalAPI
class MultiplayerServer(private val port : Int,private val clientCount : Int) {

    lateinit var server : ServerSocket

    var sockets : MutableList<Socket> = emptyList<Socket>().toMutableList()

    init {
        val builder = aSocket(SelectorManager(Dispatchers.Default))

        server = builder.tcp().bind(hostname = "127.0.0.1",port = port)
        println("Started echo telnet server at ${server.localAddress}")
        runBlockingNoJs {
            while (true) {
                val socket = server.accept()

                launch(Dispatchers.Default) {
                    println("Socket accepted: ${socket.remoteAddress}")

                    val input = socket.openReadChannel()
                    val output = socket.openWriteChannel(autoFlush = true)

                    try {
                        while (true) {
                            val line = input.readUTF8Line()

                            println("${socket.remoteAddress}: $line")
                            output.writeStringUtf8("$line\r\n")
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        socket.close()
                    }
                }
            }
        }
    }

}

@InternalAPI
class MultiplayerClient(private val ip : String, port: Int) {

    lateinit var socket : Socket

    init {
        val builder = aSocket(SelectorManager(Dispatchers.Default))
        runBlockingNoJs {
            socket = builder.tcp().connect(ip, port)
            val input = socket.openReadChannel()
            val output = socket.openWriteChannel(autoFlush = true)

            output.writeStringUtf8("hello\r\n")
            val response = input.readUTF8Line()
            println("Server said: '$response'")
        }
    }

}