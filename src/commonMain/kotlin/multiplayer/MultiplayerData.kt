package multiplayer

interface MultiplayerData {

    override fun toString(): String

    fun fromString(str:String?):MultiplayerData

}