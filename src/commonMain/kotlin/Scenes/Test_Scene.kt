package Scenes

import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import kotlinx.coroutines.*
import multiplayer.*

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {
        solidRect(10,10,color = Colors.GREEN).xy(0,0)
        //Server Test
        ///*
        Multiplayer.startSever()
        GlobalScope.launch {
            while (true){
                println(Multiplayer.read(Strg("")))
            }
        }
        //*/
        //Client Test
        /*
        Multiplayer.startClient(1,"")
        GlobalScope.launch {
            Multiplayer.sync(Strg("test"))
        }
        */
    }
}

class Strg(var d : String):MultiplayerData{
    override fun toString(): String {
        return d
    }

    override fun fromString(str: String?): MultiplayerData {
        if (!str.isNullOrEmpty())return Strg(str)
        return Strg("")
    }
}