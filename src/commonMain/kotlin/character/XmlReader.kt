package character

import com.soywiz.klock.TimeSpan
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.serialization.xml.readXml

/**
 * Read a Character with his animations and attacks from an xml file
 * See Test.xml in resources for an example xml in the right format
 */
suspend fun VfsFile.readCharacterXmlData(): CharacterXmlData {
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
    val direction = xml.attribute("direction")?.toInt() ?: 0

    //read all attacks
    val attacksFile = xml.children("attack")
    val attacks = mutableListOf<Attack>()
    attacksFile.forEach {
        val attackName = it.attribute("name") ?: ""
        val damage = it.attribute("damage")?.toInt() ?: 0
        val blockable: Boolean = it.attribute("blockable") == "true"
        val cooldown = TimeSpan(it.attribute("cooldown")?.toDouble() ?: 0.0)
        val isReady: Boolean = it.attribute("isready") == "true"

        //create character.Attack
        val newAttack = Attack(attackName, damage, blockable, cooldown, isReady)
        attacks.add(newAttack)
    }

    //create the new Player
    val standardAttack = attacks.filter { it.name == "standard" }[0]
    val specialAttack = attacks.filter { it.name == "special" }[0]
    val rangedAttack = attacks.filter { it.name == "ranged" }[0]

    val characterXmlData = CharacterXmlData(
        name,
        dbName,
        skeletonJsonFile,
        textureJsonFile,
        imageFile,
        healthpoints,
        movementSpeed,
        jumpHeight,
        direction,
        standardAttack,
        rangedAttack,
        specialAttack
    )

    return characterXmlData
}