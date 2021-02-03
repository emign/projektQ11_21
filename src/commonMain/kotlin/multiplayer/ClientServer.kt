package multiplayer

abstract class ClientServer() {
    abstract fun create()
    abstract fun send(data: MultiplayerData)
    abstract fun setGet(callback : (String)->(Unit))
}