package character

import com.soywiz.klock.TimeSpan

data class CharacterXmlData(
    val name: String,
    val dbName: String,
    val skeletonJsonFile: String,
    val textureJsonFile: String,
    val imageFile: String,
    val healthpoints: Double,
    val movementSpeed: Double,
    val jumpHeight: Double,
    var direction: Int,
    val normalAttack: Attack,
    val rangedAttack: Attack,
    val specialAttack: Attack
)

open class Attack(
    val name: String,
    val damage: Int,
    val blockable: Boolean,
    val cooldown: TimeSpan,
    val isReady: Boolean
)

class NormalAttack(
    name: String,
    damage: Int,
    blockable: Boolean,
    cooldown: TimeSpan,
    isReady: Boolean
) : Attack(name, damage, blockable, cooldown, isReady)

class RangedAttack(
    name: String,
    damage: Int,
    blockable: Boolean,
    cooldown: TimeSpan,
    isReady: Boolean
) : Attack(name, damage, blockable, cooldown, isReady)

class SpecialAttack(
    name: String,
    damage: Int,
    blockable: Boolean,
    cooldown: TimeSpan,
    isReady: Boolean
) : Attack(name, damage, blockable, cooldown, isReady)