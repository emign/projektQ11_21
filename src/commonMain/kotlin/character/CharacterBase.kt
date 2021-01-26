package character

import character.physic.Physics
import com.soywiz.korge.dragonbones.KorgeDbArmatureDisplay
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json
import com.soywiz.korma.geom.Point
import eventBus.*
import fsm.createState
import fsm.createStateManager
import kotlinx.coroutines.CoroutineScope


/**
 * Base class Character for the Player and the enemies. All objects which will move on Screen and attack etc. are inheriting this class
 * @property model A KorgeDbArmatureDisplay, basically a view which can display Dragonbones animations. You can get it by using a [KorgeDbFactory]
 * @property xmlData The data of the character. Should be read from an xml-File which contains all the data
 * @property bus The [EventBus] which is used for setting up and triggering events
 */
class CharacterBase(
    val xmlFile: String,
    val bus: EventBus,
    val scope: CoroutineScope
) : Container() {
    /**
     * create a new object of this class -> Better than direct initialization via constructor, here you can use the xmlReader
     * @param xmlFile the String-file of the character-xml. The xml has to be in the right format for reading characters
     * @param eventBus The event Bus which is used for setting up and triggering events
     */

    lateinit var model: KorgeDbArmatureDisplay
    lateinit var characterXmlData: CharacterXmlData

    fun buildXmlDataAndModel() {
        this.scope.launchImmediately {
            characterXmlData = resourcesVfs[xmlFile].readCharacterXmlData()

            val ske = resourcesVfs[characterXmlData.skeletonJsonFile].readString()
            val tex = resourcesVfs[characterXmlData.textureJsonFile].readString()
            val img = resourcesVfs[characterXmlData.imageFile].readBitmap()

            val factory = KorgeDbFactory()

            val data = factory.parseDragonBonesData(Json.parse(ske)!!)
            val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

            model = factory.buildArmatureDisplay(characterXmlData.dbName)!!
        }
    }


    /** Datermines how long we are in a state. Is set to 0 every time a new state is entered    */
    var timer: Int = 0

    /** Line on which the character is walking -> used for jumping                      */
    var baseLine: Double = this.y

    var dead: Boolean = false
    var position: Point = Point(this.x, this.y)
    var lastPosition: Point = Point(this.x, this.y)
    var newScale: Double = this.scale

    /** physics manager for checking for intersection and updating positions and velocities       */
    val activePhysics: Physics = Physics(this, bus)

    /** Make states from a manager          */
    val stateManager = createStateManager()

    val idleState = createState(stateManager)
    val walkRightState = createState(stateManager)
    val walkLeftState = createState(stateManager)
    val turnState = createState(stateManager)
    val jumpState = createState(stateManager)
    val deathState = createState(stateManager)
    val normalAttackState = createState(stateManager)
    val rangedAttackState = createState(stateManager)
    val specialAttackState = createState(stateManager)
    val damageState = createState(stateManager)


    init {
        /** set things up ...       */
        onCreate()

        /** main loop for this character        */
        addUpdater {
            onExecute()
        }
    }


    /** executed once at the beginning      */
    fun onCreate() {
        //initialize states
        initStates()
        stateManager.setStartState(idleState)
        //register physics
        addComponent(activePhysics)
        //register events
        bus.register<PlayerCollision> { onPlayerCollision(it.activePhysics) }
        bus.register<SpriteCollision> { onPlayerCollision(it.activePhysics) }
        bus.register<NormalAttackCollision> { onNormalAttackCollision(it.damage) }
        bus.register<RangedAttackCollision> { onRangedAttackCollision(it.damage) }
        bus.register<SpecialAttackCollision> { onSpecialAttackCollision(it.damage) }
    }

    /** executed every frame        */
    fun onExecute() {
        stateManager.updateCurrentState()
        updateTranslation()
    }

    /** Delete this character after dying or something else ...         */
    fun onDelete() {
        this.removeFromParent()
    }


    /** Initilize the states of the character           */
    fun initStates() {

        walkRightState.onBegin {  }
        walkRightState.onExecute {  }
        walkRightState.onEnd {  }

        walkLeftState.onBegin {  }
        walkLeftState.onExecute {  }
        walkLeftState.onEnd {  }

        idleState.onBegin { beginState_idle() }
        idleState.onExecute { executeState_idle() }
        idleState.onEnd { endState_idle() }

        turnState.onBegin { beginState_turn() }
        turnState.onExecute { executeState_turn() }
        turnState.onEnd { endState_turn() }

        jumpState.onBegin { beginState_jump() }
        jumpState.onExecute { executeState_jump() }
        jumpState.onEnd { endState_jump() }

        deathState.onBegin { beginState_death() }
        deathState.onExecute { executeState_death() }
        deathState.onEnd { endState_death() }

        normalAttackState.onBegin { beginState_normalAttack() }
        normalAttackState.onExecute { executeState_normalAttack() }
        normalAttackState.onEnd { endState_normalAttack() }

        rangedAttackState.onBegin { beginState_rangedAttack() }
        rangedAttackState.onExecute { executeState_rangedAttack() }
        rangedAttackState.onEnd { endState_rangedAttack() }

        specialAttackState.onBegin { beginState_specialAttack() }
        specialAttackState.onExecute { executeState_specialAttack() }
        specialAttackState.onEnd { endState_specialAttack() }

        damageState.onBegin { beginState_damage() }
        damageState.onExecute { executeState_damage() }
        damageState.onEnd { endState_damage() }
    }

    /** update final position of the view and scale etc.        */
    fun updateTranslation() {
        this.xy(this.position.x, this.position.y)
        this.scale = this.newScale

        this.model.xy(this.x, this.y)   //Maybe move the animation a bit, we will see...
    }


    /** kill this character     */
    fun kill() {
        //maybe add more here
        dead = true
        onDelete()
    }

    //maybe?
    fun addPhysics() {
        activePhysics.isActive = true
    }

    fun removePhysics() {
        activePhysics.isActive = false
    }


    /** checked every frame in specific states, main collision method       */
    fun calculateCollisions() {
        //
        handlePhysics()
        //check for collisions with specific types
        //activePhysics.isPlayerCollision()
        //activePhysics.isSpriteCollision()
        //apply events
        //TODO
    }


    //collision callbacks
    fun onPlayerCollision(activePhysicsOther: Physics) {
        //physics.calculate Collision and direction of collision
        //maybe change state or something...
    }

    fun onSpriteCollision(activePhysicsOther: Physics) {
        //do nothing for now -> collision with other AI objects
    }

    fun onNormalAttackCollision(damage: Double) {
        //change state to damage, play sound, ...
    }

    fun onRangedAttackCollision(damage: Double) {
        //change state to damage, play sound, ...
    }

    fun onSpecialAttackCollision(damage: Double) {
        //change state to damage, play sound, ...
    }


    /** Here's where the fun begins. Implementing all states (They're currently 9)      */

    fun beginState_idle() {
        this.timer = 0
        model.animation.play("idle")
    }

    fun executeState_idle() {
        timer += 1
    }

    fun endState_idle() { /* Nothing in here */
    }

    fun beginState_turn() {
        characterXmlData.direction = this.characterXmlData.direction xor 1
        model.animation.play("turn_${characterXmlData.direction}")
    }

    fun executeState_turn() {
        //if (model.animation.isCompleted) stateManager.doStateChange()
    }

    fun endState_turn() { /* Nothing in here */
    }


    fun beginState_jump() {
        this.timer = 0
        if (characterXmlData.direction == 1) {
            model.animation.play("jump_right", 1)
        } else if (characterXmlData.direction == 0) {
            model.animation.play("jump_left", 1)
        }
    }

    fun executeState_jump() {
        timer += 1
        if(model.animation.isCompleted) {
            stateManager.doStateChange(stateManager.stateStack[stateManager.stateStack.size - 1])
        }
    }

    fun endState_jump() { /* Nothing here */
    }


    fun beginState_death() {
        this.removePhysics()
        this.timer = 0
        model.animation.play("die", 1)
    }

    fun executeState_death() {
        timer += 1
        if (model.animation.isCompleted) {
            this.kill()
        }
    }

    fun endState_death() { /* Nothing in here */
    }


    fun beginState_normalAttack() {
        timer = 0
        model.animation.play("normal_attack", 1)
    }

    fun executeState_normalAttack() {
        timer += 1
        //check if he collides with something -> this can take damage
        if (model.animation.isCompleted) {
            stateManager.doStateChange(stateManager.stateStack[stateManager.stateStack.size - 1])
        }
    }

    fun endState_normalAttack() { /* Nothing in here */
    }


    fun beginState_rangedAttack() {
        timer = 0
        model.animation.play("ranged_attack", 1)
        //create a new flying object -> check for collision -> apply damage
    }

    fun executeState_rangedAttack() {
        timer += 1
        if (model.animation.isCompleted) {
            stateManager.doStateChange(stateManager.stateStack[stateManager.stateStack.size - 1])
        }
    }

    fun endState_rangedAttack() { /* Nothing in here */
    }


    fun beginState_specialAttack() {
        timer = 0
        model.animation.play("special_attack", 1)
        //maybe shoot something or ...
    }

    fun executeState_specialAttack() {
        timer += 1
        //check if he collides with something -> this can take damage
        if (model.animation.isCompleted) {
            stateManager.doStateChange(stateManager.stateStack[stateManager.stateStack.size - 1])
        }
    }

    fun endState_specialAttack() { /* Nothing in here */
    }


    fun beginState_damage() {
        timer = 0
        model.animation.play("get_damage", 1)
        //play sound
    }

    fun executeState_damage() {
        timer += 1
        if (model.animation.isCompleted) {
            stateManager.doStateChange(stateManager.stateStack[stateManager.stateStack.size - 1])
        }
    }

    fun endState_damage() { /* Nothing in here*/
    }

}