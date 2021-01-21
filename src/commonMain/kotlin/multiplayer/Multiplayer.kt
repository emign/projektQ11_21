package multiplayer

import com.soywiz.korio.net.*
import com.soywiz.korio.stream.*

object Multiplayer {

    var activeClient : AsyncClient? = null

    suspend fun startClient(port : Int, host : String){
        activeClient = createTcpClient(host, port)
    }

    suspend fun sync(data : MultiplayerData){
            activeClient?.writeString(data.toString()+"\n")
    }

    suspend fun startSever(port: Int = AsyncServer.ANY_PORT){
        activeClient = createTcpServer(port).accept()
    }

    suspend fun read(coder : MultiplayerData):MultiplayerData{
        return coder.fromString(activeClient?.readLine())
    }

}