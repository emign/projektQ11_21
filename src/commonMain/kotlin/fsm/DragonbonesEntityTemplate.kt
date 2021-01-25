import com.soywiz.korge.dragonbones.KorgeDbArmatureDisplay
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json
import eventBus.EventBus
import eventBus.GroundedEvent
import fsm.MakeOwnEntityTemplate
import fsm.createState
import fsm.createStateManager

//package fsm

class DragonbonesEntityTemplate(val model: KorgeDbArmatureDisplay, val bus: EventBus) {

    companion object {
        /**
         * create a new object of this class -> Better than direct initialization via constructor
         */
        suspend fun build(
            name: String,
            skeletonJsonFile: String,
            textureJsonFile: String,
            imageFile: String,
            eventBus: EventBus
        ): DragonbonesEntityTemplate {
            val ske = resourcesVfs[skeletonJsonFile].readString()
            val tex = resourcesVfs[textureJsonFile].readString()
            val img = resourcesVfs[imageFile].readBitmap()

            val factory = KorgeDbFactory()

            val data = factory.parseDragonBonesData(Json.parse(ske)!!)
            val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

            val graphic = factory.buildArmatureDisplay(name)!!

            return DragonbonesEntityTemplate(graphic, eventBus)
        }
    }

    val healthpoints: Int = 5
    var timer: Int = 0
    val movementSpeed: Double = 5.0
    val jumpHeight: Double = 70.0
    val baseLine = model.y
    var isAtMaxJumpHeight: Boolean = false
    var dead: Boolean = false

    //create states
    val stateManager = createStateManager()
    val jumpState = createState(stateManager)
    val runState = createState(stateManager)
    val deathState = createState(stateManager)
    val idleState = createState(stateManager)


    //what should be done on initialization?
    init {

        //set things up ...
        onCreate()

        //do every frame -> loop of the Entity
        model.addUpdater {
            onExecute()
        }
    }

    /**
     * Setup function which should be called once after creating a new [MakeOwnEntityTemplate]
     * This should initialize the states and set a start state
     * Maybe it can initialize even more in the future... but for now, just the states
     */
    fun onCreate() {
        initStates()
        stateManager.setStartState(idleState)
        //init physics data or collision data
    }

    /**
     * Called every frame -> main loop, should update the currentState and to maybe even more later on
     */
    fun onExecute() {
        if (model.x >= 1300) spriteCollision()
        stateManager.updateCurrentState()
    }


    /**
     * Deletes the entity after it is dead
     */
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

        idleState.onBegin { model.animation.play("steady") }
    }


    fun spriteCollision() {
        if (stateManager.getCurrentState() == runState) {
            stateManager.doStateChange(deathState)
        }
    }


    //Fun begins -> implement each state
    fun beginState_run() {
        model.animation.play("run")
    }

    fun executeState_run() {
        this.model.x += 5
        if (this.healthpoints <= 0) stateManager.doStateChange(deathState)
    }

    fun endState_run() {

    }

    fun beginState_death() {
        model.animation.play("dead", 1)
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
        model.animation.play("normalAttack", 1)
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
        bus.send(GroundedEvent())
    }

    //We can add even more states, as much as we want, e.g. hit, turn, getDamage, ...

}