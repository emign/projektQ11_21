package fsm

import com.dragonbones.model.DragonBonesData
import com.dragonbones.model.TextureAtlasData
import com.esotericsoftware.spine.readSkeletonBinary
import com.soywiz.korge.dragonbones.KorgeDbArmatureDisplay
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.position
import com.soywiz.korge.view.scale
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json

class DragonbonesEntityTemplate(val model : KorgeDbArmatureDisplay) {

    companion object {
        /**
         * create a new object of this class -> Better than direct initialization via constructor
         */
        suspend fun build(name : String, skeletonJsonFile : String, textureJsonFile : String, imageFile : String, factory : KorgeDbFactory): DragonbonesEntityTemplate {
            val ske = resourcesVfs[skeletonJsonFile].readString()
            val tex = resourcesVfs[textureJsonFile].readString()
            val img = resourcesVfs[imageFile].readBitmap()

            val data = factory.parseDragonBonesData(Json.parse(ske)!!)
            val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)
            val graphic = factory.buildArmatureDisplay(name)!!

            return DragonbonesEntityTemplate(graphic)
        }
    }

    //Entity specific variables
    val healthpoints: Int = 5
    var timer: Int = 0
    val movementSpeed: Double = 5.0
    val jumpHeight: Double = 70.0
    val baseLine = model.y
    var isAtMaxJumpHeight: Boolean = false
    var dead: Boolean = false

    //create States
    val stateManager = createStateManager()
    val jumpState = createState(stateManager)
    val runState = createState(stateManager)
    val deathState = createState(stateManager)
    val idleState = createState(stateManager)

    init {

        //set things up ...
        onCreate()

        //do every frame -> loop of the Entity
        model.addUpdater {
            onExecute()
        }
    }

    fun onCreate() {
        initStates()
        stateManager.setStartState(idleState)
    }

    /**
     * Called every frame -> main loop, should update the currentState and to maybe even more later on
     */
    fun onExecute() {
        if (model.x >= 1300) spriteCollision()
        stateManager.updateCurrentState()
    }

    fun delete() {
        this.model.removeFromParent()
        //if the entity is dead, it should not be updated any more by the Updater
        dead = true
        println("ENNNND")
    }

    fun initStates() {
        runState.onBegin { beginState_run() }
        runState.onExecute { executeState_run() }
        runState.onEnd { endState_run() }

        deathState.onBegin { beginState_death() }
        deathState.onExecute { executeState_death() }
        deathState.onEnd { endState_death() }

        jumpState.onBegin { beginState_jump() }
        jumpState.onExecute { executeState_jump() }
        jumpState.onEnd { endState_jump() }

        idleState.onBegin { model.animation.play("IDLE") }
    }

    fun spriteCollision() {
        if (stateManager.getCurrentState() == runState) {
            stateManager.doStateChange(deathState)
        }
    }


    //Fun begins -> implement each state
    fun beginState_run() {
        model.animation.play("IDLE")
    }

    fun executeState_run() {
        this.model.x += 5
        if (this.healthpoints <= 0) stateManager.doStateChange(deathState)
    }

    fun endState_run() {

    }

    fun beginState_death() {
        model.animation.play("IDLE", 1)
    }

    fun executeState_death() {
        this.timer += 1
        if (this.timer > 450) {
            delete()
        }
    }

    fun endState_death() {

    }

    fun beginState_jump() {
        model.animation.play("ATTACK", 1)
    }

    fun executeState_jump() {
        if (isAtMaxJumpHeight) {
            if (baseLine - model.y > 0) model.y += movementSpeed
            else {
                stateManager.doStateChange(idleState)
                isAtMaxJumpHeight = false
            }
        } else {
            if (baseLine - model.y >= jumpHeight) isAtMaxJumpHeight = true
            else model.y -= movementSpeed
        }
    }

    fun endState_jump() {

    }

    //We can add even more states, as much as we want, e.g. hit, turn, getDamage, ...
}