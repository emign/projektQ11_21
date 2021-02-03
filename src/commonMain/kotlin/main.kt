import actor.Player
import actor.readCharacterXmlData
import com.soywiz.klock.DateTime
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json
import org.jbox2d.common.Vec2
import physic.Listener
import physic.Physics
import kotlin.reflect.KFunction

suspend fun main() = Korge {
    val characterXmlData = resourcesVfs["Characters/Demon.xml"].readCharacterXmlData()

    val ske = resourcesVfs[characterXmlData.skeletonJsonFile].readString()
    val tex = resourcesVfs[characterXmlData.textureJsonFile].readString()
    val img = resourcesVfs[characterXmlData.imageFile].readBitmap()

    val factory = KorgeDbFactory()

    val data = factory.parseDragonBonesData(Json.parse(ske)!!)
    val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

    val model = factory.buildArmatureDisplay(characterXmlData.dbName)!!

    model.xy(500, 500)
    addChild(model)
    model.animation.play("run")

    addUpdater {
        if(views.keys.justPressed(Key.SPACE)) model.animation.play("uniqueAttack", 1)
        if(model.animation.isCompleted) model.animation.play("run")
    }
}
