package xmlReader

import com.soywiz.klock.TimeSpan
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.xml.readXml

/**
 * Read the Player/Character with his states and attacks from an xml file
 * See Test.xml in resources for an example xml in the right format
 * The Player, Attack and State class are just first ideas
 */
suspend fun VfsFile.readPlayer(): Character {
    val xml = this.readXml()

    //basic properties
    val modelFile = xml.attribute("model")
    val model = resourcesVfs[modelFile.toString()].readBitmap()
    val healthpoints = xml.attribute("healthpoints")?.toDouble() ?: 0.0
    val movementSpeed = xml.attribute("movementSpeed")?.toDouble() ?: 0.0
    val jumpHeight = xml.attribute("jumpHeight")?.toDouble() ?: 0.0

    //read all attacks
    val attacksFile = xml.children("attack")
    val attacks = mutableListOf<Attack>()
    attacksFile.forEach {
        val name = it.attribute("name") ?: ""
        val damage = it.attribute("damage")?.toInt() ?: 0
        val blockable: Boolean = it.attribute("blockable") == "true"
        val ranged: Boolean = it.attribute("ranged") == "true"
        val isSuperAttack: Boolean = it.attribute("isSuperAttack") == "true"
        val isFinisher: Boolean = it.attribute("isFinisher") == "true"
        val cooldown = TimeSpan(it.attribute("cooldown")?.toDouble() ?: 0.0)
        val isReady: Boolean = it.attribute("isready") == "true"

        //create Attack
        val newAttack = Attack(name, damage, blockable, ranged, isSuperAttack, isFinisher, cooldown, isReady)
        attacks.add(newAttack)
    }

    //read all states
    val statesFile = xml.children("state")
    val states = mutableListOf<State>()
    statesFile.forEach {
        val name = it.attribute("name") ?: ""
        val duration = it.attribute("duration")?.toDouble() ?: 0.0

        val newState = State(name, duration)
        states.add(newState)
    }

    //create the new Player
    val standardAttack = attacks.filter { it.name == "standard" }[0]
    val specialAttack = attacks.filter { it.name == "special" }[0]
    val rangedAttack = attacks.filter { it.name == "ranged" }[0]
    val finisherAttack = attacks.filter { it.name == "finish" }[0]

    val player = Character(
        model,
        healthpoints,
        movementSpeed,
        jumpHeight,
        standardAttack,
        specialAttack,
        rangedAttack,
        finisherAttack,
        states
    )
    return player

}