import com.soywiz.korge.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korge.view.views
import com.soywiz.korio.file.std.resourcesVfs

suspend fun main() = Korge(Korge.Config(module = GameModule))
