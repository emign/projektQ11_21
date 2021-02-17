package actor.actors

import actor.ActorXmlData
import actor.MovingActor
import actor.readCharacterXmlData
import com.soywiz.korge.dragonbones.KorgeDbArmatureDisplay
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.Container
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json
import kotlinx.coroutines.CoroutineScope
import physic.internal.Physics

class Bullet(parent: Container, override val model: KorgeDbArmatureDisplay, scope: CoroutineScope, actorXmlData: ActorXmlData): MovingActor(parent, scope, actorXmlData) {

    init {
        onCreate()
    }

    companion object {
        suspend fun build(xmlFile: String, scope: CoroutineScope, parent: Container): Bullet {
            val characterXmlData = resourcesVfs[xmlFile].readCharacterXmlData()

            val ske = resourcesVfs[characterXmlData.skeletonJsonFile].readString()
            val tex = resourcesVfs[characterXmlData.textureJsonFile].readString()
            val img = resourcesVfs[characterXmlData.imageFile].readBitmap()

            val factory = KorgeDbFactory()

            val data = factory.parseDragonBonesData(Json.parse(ske)!!)
            val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

            val model = factory.buildArmatureDisplay(characterXmlData.dbName)!!
            return Bullet(parent, model, scope, characterXmlData)
        }
    }

    override fun initEvents() {
        TODO("Not yet implemented")
    }

    override fun enablePhysics() {
        TODO("Not yet implemented")
    }

    override fun disablePhysics() {
        TODO("Not yet implemented")
    }

    override fun onPlayerCollision(aabbOther: Physics) {
        TODO("Not yet implemented")
    }

    override fun onEnemyCollision(aabbOther: Physics) {
        TODO("Not yet implemented")
    }

    override fun onGroundCollision() {
        TODO("Not yet implemented")
    }

    override fun onPlatformCollision(platform: Physics) {
        TODO("Not yet implemented")
    }

    override fun onNormalAttackCollision(damage: Double) {
        TODO("Not yet implemented")
    }

    override fun onRangedAttackCollision(damage: Double) {
        TODO("Not yet implemented")
    }

    override fun onSpecialAttackCollision(damage: Double) {
        TODO("Not yet implemented")
    }

    override fun beginState_idle() {
        TODO("Not yet implemented")
    }

    override fun executeState_idle(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_idle() {
        TODO("Not yet implemented")
    }

    override fun beginState_walk() {
        TODO("Not yet implemented")
    }

    override fun executeState_walk(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_walk() {
        TODO("Not yet implemented")
    }

    override fun beginState_turn() {
        TODO("Not yet implemented")
    }

    override fun executeState_turn(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_turn() {
        TODO("Not yet implemented")
    }

    override fun beginState_jump() {
        TODO("Not yet implemented")
    }

    override fun executeState_jump(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_jump() {
        TODO("Not yet implemented")
    }

    override fun beginState_die() {
        TODO("Not yet implemented")
    }

    override fun executeState_die(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_die() {
        TODO("Not yet implemented")
    }

    override fun beginState_normalAttack() {
        TODO("Not yet implemented")
    }

    override fun executeState_normalAttack(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_normalAttack() {
        TODO("Not yet implemented")
    }

    override fun beginState_rangedAttack() {
        TODO("Not yet implemented")
    }

    override fun executeState_rangedAttack(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_rangedAttack() {
        TODO("Not yet implemented")
    }

    override fun beginState_specialAttack() {
        TODO("Not yet implemented")
    }

    override fun executeState_specialAttack(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_specialAttack() {
        TODO("Not yet implemented")
    }

    override fun beginState_getDamage() {
        TODO("Not yet implemented")
    }

    override fun executeState_getDamage(dt: Double) {
        TODO("Not yet implemented")
    }

    override fun endState_getDamage() {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        addChild(model)
        model.animation.play("steady")
    }

    override fun onExecute(dt: Double) {
        //physic.getPhysics.update(dt)
    }

    override fun onDelete() {
        TODO("Not yet implemented")
    }

    override fun updateGraphics() {
        TODO("Not yet implemented")
    }

    override fun kill() {
        TODO("Not yet implemented")
    }
}