package Xml_API

data class Attack(
    val damage: Double,
    val blockable: Boolean,
    val cooldown: Double,
    val isReady: Boolean
) : XmlData {
    override fun getData(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()

        map.put("damage", damage)
        map.put("blockable", blockable)
        map.put("cooldown", cooldown)
        map.put("isready", isReady)

        return map
    }
}

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
) : XmlData {
    override fun getData(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()

        map.put("name", name)
        map.put("dbName", dbName)
        map.put("skeletonJsonFile", skeletonJsonFile)
        map.put("textureJsonFile", textureJsonFile)
        map.put("imageFile", imageFile)
        map.put("healthpoints", healthpoints)
        map.put("movementSpeed", movementSpeed)
        map.put("jumpHeight", jumpHeight)
        map.put("normalAttack", normalAttack.getData())
        map.put("rangedAttack", rangedAttack.getData())
        map.put("specialAttack", specialAttack.getData())

        return map
    }

}
