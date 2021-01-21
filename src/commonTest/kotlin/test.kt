import com.soywiz.klock.*
import com.soywiz.korge.input.*
import com.soywiz.korge.tests.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import xmlReader.readPlayer
import kotlin.test.Test
import kotlin.test.assertEquals


class MyTest : ViewsForTesting() {

    @Test
    fun testCharacterXMLreader() = viewsTest{
        val character = resourcesVfs["Characters/Test.xml"].readPlayer()
        assertEquals(100.0, character.healthpoints)
        assertEquals(5.0, character.movementSpeed)
        assertEquals(15.0, character.jumpHeight)

        assertEquals("standard", character.standardAttack.name)
        assertEquals("special", character.specialAttack.name)
        assertEquals("ranged", character.rangedAttack.name)
        assertEquals("finish", character.finisherAttack.name)

        assertEquals(3, character.standardAttack.damage)
        assertEquals(10, character.specialAttack.damage)
        assertEquals(5, character.rangedAttack.damage)
        assertEquals(50, character.finisherAttack.damage)

        assertEquals(false, character.standardAttack.ranged)
        assertEquals(true, character.specialAttack.ranged)
        assertEquals(false, character.rangedAttack.ranged)
        assertEquals(false, character.finisherAttack.ranged)

        assertEquals(5000.milliseconds, character.standardAttack.cooldown)
        assertEquals(20.seconds, character.specialAttack.cooldown)
        assertEquals(15.seconds, character.rangedAttack.cooldown)
        assertEquals(25.seconds, character.finisherAttack.cooldown)

        assertEquals(true, character.standardAttack.isReady)
        assertEquals(false, character.specialAttack.isReady)
        assertEquals(true, character.rangedAttack.isReady)
        assertEquals(false, character.finisherAttack.isReady)

        assertEquals("walk", character.states[0].name)
        assertEquals("idle", character.states[1].name)
        assertEquals("jump", character.states[2].name)

    }
}