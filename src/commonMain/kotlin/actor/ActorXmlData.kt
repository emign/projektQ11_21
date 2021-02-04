package actor

import com.soywiz.klock.TimeSpan
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.serialization.xml.readXml

/**
 * Wrapper for storing information from an xml file read by [readCharacterXmlData]
 */
data class ActorXmlData(
    val name: String,
    val dbName: String,
    val skeletonJsonFile: String,
    val textureJsonFile: String,
    val imageFile: String,
    val healthpoints: Double,
    val movementSpeed: Double,
    val jumpHeight: Double,
    val normalAttack: Attack,
    val rangedAttack: Attack,
    val specialAttack: Attack
)

open class Attack(
    val name: String,
    val damage: Double,
    val blockable: Boolean,
    val cooldown: Double,
    val isReady: Boolean
)

class NormalAttack(
    name: String,
    damage: Double,
    blockable: Boolean,
    cooldown: Double,
    isReady: Boolean
) : Attack(name, damage, blockable, cooldown, isReady)

class RangedAttack(
    name: String,
    damage: Double,
    blockable: Boolean,
    cooldown: Double,
    isReady: Boolean
) : Attack(name, damage, blockable, cooldown, isReady)

class SpecialAttack(
    name: String,
    damage: Double,
    blockable: Boolean,
    cooldown: Double,
    isReady: Boolean
) : Attack(name, damage, blockable, cooldown, isReady)

/**
 * Read a Character with his animations and attacks from an xml file
 * See Test.xml in resources for an example xml in the right format
 */
suspend fun VfsFile.readCharacterXmlData(): ActorXmlData {
    val xml = this.readXml()

    //basic properties
    val name = xml.attribute("name") ?: ""
    val dbName = xml.attribute("DbName") ?: ""
    val skeletonJsonFile = xml.attribute("skeletonJsonFile") ?: ""
    val textureJsonFile = xml.attribute("textureJsonFile") ?: ""
    val imageFile = xml.attribute("imageFile") ?: ""
    val healthpoints = xml.attribute("healthpoints")?.toDouble() ?: 0.0
    val movementSpeed = xml.attribute("movementSpeed")?.toDouble() ?: 0.0
    val jumpHeight = xml.attribute("jumpHeight")?.toDouble() ?: 0.0

    //read all attacks
    val attacksFile = xml.children("attack")
    val attacks = mutableListOf<Attack>()
    attacksFile.forEach {
        val attackName = it.attribute("name") ?: ""
        val damage = it.attribute("damage")?.toDouble() ?: 0.0
        val blockable: Boolean = it.attribute("blockable") == "true"
        val cooldown = it.attribute("cooldown")?.toDouble() ?: 0.0
        val isReady: Boolean = it.attribute("isready") == "true"

        //create actor.Attack
        val newAttack = Attack(attackName, damage, blockable, cooldown, isReady)
        attacks.add(newAttack)
    }

    //create the new Player
    val standardAttack = attacks.filter { it.name == "standard" }[0]
    val specialAttack = attacks.filter { it.name == "special" }[0]
    val rangedAttack = attacks.filter { it.name == "ranged" }[0]

    val characterXmlData = ActorXmlData(
        name,
        dbName,
        skeletonJsonFile,
        textureJsonFile,
        imageFile,
        healthpoints,
        movementSpeed,
        jumpHeight,
        standardAttack,
        rangedAttack,
        specialAttack
    )

    return characterXmlData
}