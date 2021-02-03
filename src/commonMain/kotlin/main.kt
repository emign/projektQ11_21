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

suspend fun main() = Korge(Korge.Config(GameModule))
