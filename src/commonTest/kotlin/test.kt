import actor.readCharacterXmlData
import com.soywiz.korge.tests.ViewsForTesting
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.test.Test
import kotlin.test.assertEquals


class MyTest : ViewsForTesting() {

    @Test
    fun testCharacterXMLreader() = viewsTest{
        val character = resourcesVfs["Characters/Demon.xml"].readCharacterXmlData()

        assertEquals("Demon", character.name)
        assertEquals("armatureName", character.dbName)
        assertEquals("Demon_ske.json", character.skeletonJsonFile)
        assertEquals("Demon_tex.json", character.textureJsonFile)
        assertEquals("Demon_tex.png", character.imageFile)
        assertEquals(100.0, character.healthpoints)
        assertEquals(5.0, character.movementSpeed)
        assertEquals(15.0, character.jumpHeight)

        assertEquals("standard", character.normalAttack.name)
        assertEquals(3.0, character.normalAttack.damage)
        assertEquals(true, character.normalAttack.blockable)
        assertEquals(5.0, character.normalAttack.cooldown)
        assertEquals(true, character.normalAttack.isReady)

        assertEquals("ranged", character.rangedAttack.name)
        assertEquals(5.0, character.rangedAttack.damage)
        assertEquals(false, character.rangedAttack.blockable)
        assertEquals(10.0, character.rangedAttack.cooldown)
        assertEquals(true, character.rangedAttack.isReady)

        assertEquals("special", character.specialAttack.name)
        assertEquals(10.0, character.specialAttack.damage)
        assertEquals(false, character.specialAttack.blockable)
        assertEquals(20.0, character.specialAttack.cooldown)
        assertEquals(false, character.specialAttack.isReady)

    }
}