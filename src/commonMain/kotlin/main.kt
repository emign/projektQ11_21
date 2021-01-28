import com.soywiz.korge.*
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.position
import com.soywiz.korge.view.scale
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json

suspend fun main() = Korge(Korge.Config(GameModule)) /*{

    val ske = resourcesVfs["DragonBones/Character_ske.json"].readString()
    val tex = resourcesVfs["DragonBones/Character_tex.json"].readString()
    val img = resourcesVfs["DragonBones/Character_tex.png"].readBitmap()

    val factory = KorgeDbFactory()

    val data = factory.parseDragonBonesData(Json.parse(ske)!!)
    val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

    val graphic = factory.buildArmatureDisplay("Armature")!!

    graphic.apply { position(500, 500)
        scale(1.0)
    }

    this += graphic
    graphic.animation.play("animtion0")
}*/