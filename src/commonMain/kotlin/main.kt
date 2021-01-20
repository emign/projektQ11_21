import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*
import xmlReader.readPlayer

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {

	//test if the reader works correctly -> it does, all data is correctly read

	val character = resourcesVfs["Test.xml"].readPlayer()
	addChild(Image(character.model).apply { scale = 0.7 })

	println(character.healthpoints)
	println(character.movementSpeed)
	println(character.jumpHeight)

	println(character.standardAttack.name)
	println(character.standardAttack.damage)
	println(character.standardAttack.blockable)
	println(character.standardAttack.ranged)
	println(character.standardAttack.isSuperAttack)
	println(character.standardAttack.isFinisher)
	println(character.standardAttack.cooldown)
	println(character.standardAttack.isReady)

	println(character.specialAttack.name)
	println(character.specialAttack.damage)
	println(character.specialAttack.blockable)
	println(character.specialAttack.ranged)
	println(character.specialAttack.isSuperAttack)
	println(character.specialAttack.isFinisher)
	println(character.specialAttack.cooldown)
	println(character.specialAttack.isReady)

	println(character.rangedAttack.name)
	println(character.rangedAttack.damage)
	println(character.rangedAttack.blockable)
	println(character.rangedAttack.ranged)
	println(character.rangedAttack.isSuperAttack)
	println(character.rangedAttack.isFinisher)
	println(character.rangedAttack.cooldown)
	println(character.rangedAttack.isReady)

	println(character.finisherAttack.name)
	println(character.finisherAttack.damage)
	println(character.finisherAttack.blockable)
	println(character.finisherAttack.ranged)
	println(character.finisherAttack.isSuperAttack)
	println(character.finisherAttack.isFinisher)
	println(character.finisherAttack.cooldown)
	println(character.finisherAttack.isReady)

	character.states.forEach {
		println(it.name)
		println(it.duration)
	}
}