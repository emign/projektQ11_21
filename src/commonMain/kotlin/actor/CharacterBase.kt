package actor

import com.soywiz.korge.box2d.*
import com.soywiz.korge.dragonbones.KorgeDbArmatureDisplay
import com.soywiz.korge.dragonbones.KorgeDbFactory
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json
import eventBus.*
import fsm.*
import kotlinx.coroutines.CoroutineScope
import org.jbox2d.dynamics.*
import physic.Physics


/**
 * Base class Character for the Player and the enemies. Just a little example of what a Player or Enemy class could look like
 * @property model A KorgeDbArmatureDisplay, basically a view which can display Dragonbones animations. You can get it by using a [KorgeDbFactory]
 * @property xmlData The data of the character. Should be read from an xml-File which contains all the data
 * @property scope The [CoroutineScope] which is used for setting up and triggering events
 */
class CharacterBase(
    val model: KorgeDbArmatureDisplay,
    xmlData: ActorXmlData,
    scope: CoroutineScope
) : MovingActor(scope, xmlData) {
    /**
     * create a new object of this class -> Better than direct initialization via constructor, here you can use the xmlReader
     * @param xmlFile the String-file of the character-xml. The xml has to be in the right format for reading characters
     * @param scope The current scope where this actor is loaded on. Used for the [EventBus]
     */
    companion object {
        suspend fun build(xmlFile: String, scope: CoroutineScope): CharacterBase {
            val characterXmlData = resourcesVfs[xmlFile].readCharacterXmlData()

            val ske = resourcesVfs[characterXmlData.skeletonJsonFile].readString()
            val tex = resourcesVfs[characterXmlData.textureJsonFile].readString()
            val img = resourcesVfs[characterXmlData.imageFile].readBitmap()

            val factory = KorgeDbFactory()

            val data = factory.parseDragonBonesData(Json.parse(ske)!!)
            val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

            val model = factory.buildArmatureDisplay(characterXmlData.dbName)!!
            return CharacterBase(model, characterXmlData, scope)
        }
    }


    init {
        /** set things up ...       */
        onCreate()

        /** main loop for this character        */
        addUpdater {
            onExecute(it.milliseconds)
        }
    }


    /** executed once at the beginning      */
    override fun onCreate() {
        //initialize states
        setStartState(idleState)

        //register physics -> TODO
        //addComponent(activePhysics)

        //register events
        initEvents()

        addChild(model)
    }

    /** executed every frame        */
    override fun onExecute(dt: Double) {
        updateCurrentState()
        updateGraphics()
    }

    /** Delete this character after dying or something else ...         */
    override fun onDelete() {
        this.removeFromParent()
    }


    /** update final position of the view and scale etc.        */
    override fun updateGraphics() {
        this.xy(this.position.x, this.position.y)
        this.scale = this.newScale

        this.model.xy(this.x, this.y)   //Maybe move the animation a bit, we will see...
    }


    /** kill this character     */
    override fun kill() {
        //maybe add more here
        dead = true
        onDelete()
    }

    override val physics: Physics = Physics()

    override fun initEvents() {
        //bus.register<PlayerCollision> { onPlayerCollision(it.activePhysics) }
        //bus.register<SpriteCollision> { onPlayerCollision(it.activePhysics) }
        bus.register<NormalAttackCollision> { onNormalAttackCollision(it.damage) }
        bus.register<RangedAttackCollision> { onRangedAttackCollision(it.damage) }
        bus.register<SpecialAttackCollision> { onSpecialAttackCollision(it.damage) }

        bus.register<StateTransition> { doStateChange(it.state) }
    }

    //maybe?
    override fun enablePhysics() {
        //activePhysics.isActive = true
    }

    override fun disablePhysics() {
        //activePhysics.isActive = false
    }


    /** checked every frame in specific states, main collision method       */
    fun calculateCollisions() {
        //check for collisions with specific types
        //activePhysics.isPlayerCollision()
        //activePhysics.isSpriteCollision()
        //apply events
        //TODO
    }


    //collision callbacks
    override fun onPlayerCollision(/*activePhysicsOther: Physics*/) {
        //physics.calculate Collision and direction of collision
        //maybe change state or something...
    }

    override fun onEnemyCollision(/*activePhysicsOther: Physics*/) {
        //do nothing for now -> collision with other AI objects
    }

    override fun onGroundCollision() {
        TODO("Not yet implemented")
    }

    override fun onPlatformCollision() {
        TODO("Not yet implemented")
    }

    override fun onNormalAttackCollision(damage: Double) {
        //change state to damage, play sound, ...
    }

    override fun onRangedAttackCollision(damage: Double) {
        //change state to damage, play sound, ...
    }

    override fun onSpecialAttackCollision(damage: Double) {
        //change state to damage, play sound, ...
    }


    /** Here's where the fun begins. Implementing all states (They're currently 9)      */

    override fun beginState_idle() {
        println("Ich beginne nichts zu tun")
        this.timer = 0
        model.animation.play("steady")
    }

    override fun executeState_idle() {
        println("Ich bin dabei, nichts zu tun")
        timer += 1
    }

    override fun endState_idle() { /* Nothing in here */
        println("Ich höre auf nichts zu tun")
    }

    override fun beginState_walk() {
        println("Ich beginne zu laufen")
        model.animation.play("run")
    }

    override fun executeState_walk() {
        println("Ich bin dabei zu laufen")
        position.x += 5
    }

    override fun endState_walk() {
        println("Ich höre auf zu laufen")
    }

    override fun beginState_turn() {
        direction = this.direction xor 1
        model.animation.play("turn_${direction}")
    }

    override fun executeState_turn() {
        //if (model.animation.isCompleted) stateManager.doStateChange()
    }

    override fun endState_turn() { /* Nothing in here */
    }


    override fun beginState_jump() {
        this.timer = 0
        if (direction == 1) {
            model.animation.play("jump_right", 1)
        } else if (direction == 0) {
            model.animation.play("jump_left", 1)
        }
    }

    override fun executeState_jump() {
        timer += 1
        if (model.animation.isCompleted) {
            doStateChange(manager.stateStack[manager.stateStack.size - 1])
        }
    }

    override fun endState_jump() { /* Nothing here */
    }


    override fun beginState_die() {
        this.disablePhysics()
        this.timer = 0
        model.animation.play("die", 1)
    }

    override fun executeState_die() {
        timer += 1
        if (model.animation.isCompleted) {
            this.kill()
        }
    }

    override fun endState_die() { /* Nothing in here */
    }


    override fun beginState_normalAttack() {
        timer = 0
        model.animation.play("normal_attack", 1)
    }

    override fun executeState_normalAttack() {
        timer += 1
        //check if he collides with something -> this can take damage
        if (model.animation.isCompleted) {
            doStateChange(manager.stateStack[manager.stateStack.size - 1])
        }
    }

    override fun endState_normalAttack() { /* Nothing in here */
    }


    override fun beginState_rangedAttack() {
        timer = 0
        model.animation.play("ranged_attack", 1)
        //create a new flying object -> check for collision -> apply damage
    }

    override fun executeState_rangedAttack() {
        timer += 1
        if (model.animation.isCompleted) {
            doStateChange(manager.stateStack[manager.stateStack.size - 1])
        }
    }

    override fun endState_rangedAttack() { /* Nothing in here */
    }


    override fun beginState_specialAttack() {
        timer = 0
        model.animation.play("special_attack", 1)
        //maybe shoot something or ...
    }

    override fun executeState_specialAttack() {
        timer += 1
        //check if he collides with something -> this can take damage
        if (model.animation.isCompleted) {
            doStateChange(manager.stateStack[manager.stateStack.size - 1])
        }
    }

    override fun endState_specialAttack() { /* Nothing in here */
    }


    override fun beginState_getDamage() {
        timer = 0
        println("Ich beginne Schaden zu erhalten")
        model.animation.play("win", 1)
        //model.animation.play("get_damage", 1)
        //play sound
    }

    override fun executeState_getDamage() {
        println("Ich bin dabei Schaden zu erhalten")
        timer += 1
        if (model.animation.isCompleted) {
            bus.send(StateTransition(idleState))
        }
    }

    override fun endState_getDamage() { /* Nothing in here*/
        println("Ich höre auf Schaden zu erhalten")
    }

}