import com.soywiz.korge.*
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.position
import com.soywiz.korge.view.scale
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json

suspend fun main() = Korge(){
    val ske = resourcesVfs["Dragonbones_Test/HandTest_ske.json"].readString()
    val tex = resourcesVfs["Dragonbones_Test/HandTest_tex.json"].readString()
    val img = resourcesVfs["Dragonbones_Test/HandTest_tex.png"].readBitmap()

    val factory = KorgeDbFactory()
    val data = factory.parseDragonBonesData(Json.parse(ske)!!)
    val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

    val graphic = factory.buildArmatureDisplay("Armature")!!.position(500,500).scale(0.6)
    graphic.animation.play("ATTACK")
    addChild(graphic)

}