import eventBus.*
import multiplayer.Multiplayer

object MultiplayerMain {
    var bus : EventBus
    var isServer = false
    lateinit var clientServer: ClientServer

    init {
        while (!Multiplayer.initalized){}
        bus = Multiplayer.bus
        bus.register<Multiplayer_StartClient>(){
            isServer = false
            clientServer=Client(Multiplayer.port,Multiplayer.host)
        }
        bus.register<Multiplayer_StartServer> {
            isServer=true
            clientServer=Server(Multiplayer.port,Multiplayer.host)
        }
        bus.register<Multiplayer_Stop> {
            clientServer.stop()
        }
        bus.register<Multiplayer_Get> { data ->
            clientServer.send(data.toString())
        }
    }
    fun receive(str:String){
        bus.send(Multiplayer_Send(str))
    }
}

open class ClientServer(){
    open fun stop(){}
    open suspend fun send(str: String){}
}