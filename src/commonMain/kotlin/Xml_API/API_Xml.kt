import Xml_API.*
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.xml.readXml
import kotlin.coroutines.AbstractCoroutineContextKey

object XmlReadWrite {
    suspend fun read(file: String, type: XmlType): XmlData {
        val XmlFile = resourcesVfs[file].readXml()

        when (type) {
            XmlType.CHARACTER -> {

                //basic properties
                val name = XmlFile.attribute("name") ?: ""
                val dbName = XmlFile.attribute("DbName") ?: ""
                val skeletonJsonFile = XmlFile.attribute("skeletonJsonFile") ?: ""
                val textureJsonFile = XmlFile.attribute("textureJsonFile") ?: ""
                val imageFile = XmlFile.attribute("imageFile") ?: ""
                val healthpoints = XmlFile.attribute("healthpoints")?.toDouble() ?: 0.0
                val movementSpeed = XmlFile.attribute("movementSpeed")?.toDouble() ?: 0.0
                val xSpeedIncrease = XmlFile.attribute("xSpeedIncrease")?.toDouble() ?: 0.0
                val jumpHeight = XmlFile.attribute("jumpHeight")?.toDouble() ?: 0.0


                //read all normal attacks
                val normalAttackFile = XmlFile.child("normalAttack")
                val normalDamage = normalAttackFile?.attribute("damage")?.toDouble() ?: 0.0
                val normalBlockable: Boolean = normalAttackFile?.attribute("blockable") == "true"
                val normalCooldown = normalAttackFile?.attribute("cooldown")?.toDouble() ?: 0.0
                val normalIsReady: Boolean = normalAttackFile?.attribute("isready") == "true"

                //create actor.Attack
                val normalAttack = Attack(normalDamage, normalBlockable, normalCooldown, normalIsReady)


                //read all ranged attacks
                val rangedAttackFile = XmlFile.child("rangedAttack")

                val rangedDamage = rangedAttackFile?.attribute("damage")?.toDouble() ?: 0.0
                val rangedBlockable: Boolean = rangedAttackFile?.attribute("blockable") == "true"
                val rangedCooldown = rangedAttackFile?.attribute("cooldown")?.toDouble() ?: 0.0
                val rangedIsReady: Boolean = rangedAttackFile?.attribute("isready") == "true"

                //create actor.Attack
                val rangedAttack = Attack(rangedDamage, rangedBlockable, rangedCooldown, rangedIsReady)


                //read all special attacks
                val specialAttackFile = XmlFile.child("specialAttack")

                val specialDamage = specialAttackFile?.attribute("damage")?.toDouble() ?: 0.0
                val specialBlockable: Boolean = specialAttackFile?.attribute("blockable") == "true"
                val specialCooldown = specialAttackFile?.attribute("cooldown")?.toDouble() ?: 0.0
                val specialIsReady: Boolean = specialAttackFile?.attribute("isready") == "true"

                //create actor.Attack
                val specialAttack = Attack(specialDamage, specialBlockable, specialCooldown, specialIsReady)


                val characterXmlData = ActorXmlData(
                    name,
                    dbName,
                    skeletonJsonFile,
                    textureJsonFile,
                    imageFile,
                    healthpoints,
                    movementSpeed,
                    jumpHeight,
                    normalAttack,
                    rangedAttack,
                    specialAttack
                )

                return characterXmlData
            }

            XmlType.PROGRESS -> {
                val levelsFinished = XmlFile.attribute("levelsFinished")?.toInt() ?: 0
                val enemiesKilled = XmlFile.attribute("enemiesKilled")?.toInt() ?: 0
                val playTime = XmlFile.attribute("playTime")?.toDouble() ?: 0.0

                val skins = mutableListOf<String>()
                XmlFile.child("skinsUnlocked")?.children("skin")?.forEach {
                    val skinName = it.attribute("name") ?: ""
                    skins.add(skinName)
                }
                val progress = Progress(levelsFinished, skins, enemiesKilled, playTime)
                return progress
            }
        }
    }

    suspend fun write(data: XmlData, file: String, type: XmlType) {
        val xmlFile = resourcesVfs[file]

        val xmlData = data.getData()

        val keys = xmlData.keys.toList()
        val values = xmlData.values.toList()

        var str = ""

        when (type) {
            XmlType.CHARACTER -> {
                str += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                str += "<player " + writeKeyValuePair(keys[0], values[0]) + " " + writeKeyValuePair(
                    keys[1],
                    values[1]
                ) + " " + writeKeyValuePair(keys[2], values[2]) + " " + writeKeyValuePair(
                    keys[3],
                    values[3]
                ) + " " + writeKeyValuePair(keys[4], values[4]) + " " + writeKeyValuePair(
                    keys[5],
                    values[5]
                ) + " " + writeKeyValuePair(keys[6], values[6]) + " " + writeKeyValuePair(keys[7], values[7]) + ">\n\t"

                str += "<normalAttack "
                (values[8] as Map<String, Any>).forEach {
                    str += writeKeyValuePair(it.key, it.value) + " "
                }
                str += ">\n\t</normalAttack>\n\t"

                str += "<rangedAttack "
                (values[9] as Map<String, Any>).forEach {
                    str += writeKeyValuePair(it.key, it.value) + " "
                }
                str += ">\n\t</rangedAttack>\n\t"

                str += "<specialAttack "
                (values[10] as Map<String, Any>).forEach {
                    str += writeKeyValuePair(it.key, it.value) + " "
                }
                str += ">\n\t</specialAttack>"

                str += "\n</player>"
            }

            XmlType.PROGRESS -> {
                str += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                str += "<Progress " + writeKeyValuePair(keys[0], values[0]) + " " + writeKeyValuePair(
                    keys[1],
                    values[1]
                ) + " " + writeKeyValuePair(keys[2], values[2]) + ">\n\t"
                str += "<skinsUnlocked>\n"
                (values[3] as List<*>).forEach {
                    str += "\t\t<skin name = \"${it as String}\">\n\t\t</skin>\n"
                }
                str += "\t</skinsUnlocked>\n</Progress>"
            }
        }
        xmlFile.writeString(str)
    }

    private fun writeKeyValuePair(key: String, value: Any): String {
        return "$key=\"$value\""
    }
}