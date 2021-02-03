package multiplayer

import eventBus.EventBus
import kotlin.properties.Delegates

//Object for connecting the Modules
object Multiplayer {
    lateinit var bus : EventBus
    var port : Int = 0
    var host : String = ""

    var initalized = false

    fun init(eventBus : EventBus,inport:Int,inhost:String){
        bus=eventBus
        port=inport
        host=inhost
        initalized=true
    }
}