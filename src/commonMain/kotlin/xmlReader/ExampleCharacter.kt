package xmlReader

import com.soywiz.klock.TimeSpan
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.xml.readXml

//example classes


data class Character(
    val model: Bitmap,
    val healthpoints: Double,
    val movementSpeed: Double,
    val jumpHeight: Double,
    val standardAttack: Attack,
    val specialAttack: Attack,
    val rangedAttack: Attack,
    val finisherAttack: Attack,
    val states: MutableList<State>
)

data class Attack(
    val name: String,
    val damage: Int,
    val blockable: Boolean,
    val ranged: Boolean,
    val isSuperAttack: Boolean,
    val isFinisher: Boolean,
    val cooldown: TimeSpan,
    val isReady: Boolean
)

data class State(val name: String, val duration: Double)


