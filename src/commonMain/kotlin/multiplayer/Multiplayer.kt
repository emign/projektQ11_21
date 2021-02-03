package multiplayer

import kotlinx.coroutines.*


class Multiplayer(private val ip : String, private val port : Int, private val server : Boolean=false,private val scope: CoroutineScope) {

    private var clientServer : ClientServer = if (server) {
        Server(ip,port,scope)
    }else{
        Client(ip,port,scope)
    }

    init {
        clientServer.create()
    }

    //send Data to the other Side
    fun send(data: MultiplayerData){
        clientServer.send(data)
    }

    //Set a Callback for receiving the values
    fun setCallback(callback:(String)->(Unit)){
        clientServer.setGet(callback)
    }

}