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
import physic.Direction
import physic.Physics


/**
 * Base class Character for the Player and the enemies. Just a little example of what a Player or Enemy class could look like
 * @property model A KorgeDbArmatureDisplay, basically a view which can display Dragonbones animations. You can get it by using a [KorgeDbFactory]
 * @property xmlData The data of the character. Should be read from an xml-File which contains all the data
 * @property scope The [CoroutineScope] which is used for setting up and triggering events
 */
class Enemy(
    override val model: KorgeDbArmatureDisplay,
    xmlData: ActorXmlData,
    scope: CoroutineScope
) : MovingActor(scope, xmlData) {
    /**
     * create a new object of this class -> Better than direct initialization via constructor, here you can use the xmlReader
     * @param xmlFile the String-file of the character-xml. The xml has to be in the right format for reading characters
     * @param scope The current scope where this actor is loaded on. Used for the [EventBus]
     */
    companion object {
        suspend fun build(xmlFile: String, scope: CoroutineScope): Enemy {
            val characterXmlData = resourcesVfs[xmlFile].readCharacterXmlData()

            val ske = resourcesVfs[characterXmlData.skeletonJsonFile].readString()
            val tex = resourcesVfs[characterXmlData.textureJsonFile].readString()
            val img = resourcesVfs[characterXmlData.imageFile].readBitmap()

            val factory = KorgeDbFactory()

            val data = factory.parseDragonBonesData(Json.parse(ske)!!)
            val atlas = factory.parseTextureAtlasData(Json.parse(tex)!!, img)

            val model = factory.buildArmatureDisplay(characterXmlData.dbName)!!
            return Enemy(model, characterXmlData, scope)
        }
    }

   // override val physics: Physics = Physics(this, Physics.PhysicsInfo(position.x, position.x + width, position.y, position.y + height, position, velocity, gravity))


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
        initPhysics()

        //register events
        initEvents()

        addChild(model)
    }

    /** executed every frame        */
    override fun onExecute(dt: Double) {
        updateCurrentState(dt)
        //physics.update(dt)
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

        this.model.xy(0, 0)   //Maybe move the animation a bit, we will see...
    }


    /** kill this character     */
    override fun kill() {
        //maybe add more here
        dead = true
        onDelete()
    }

    override fun initEvents() {
        bus.register<PlayerCollision> { onPlayerCollision(it.aabb) }
        bus.register<EnemyCollision> { onPlayerCollision(it.aabb) }
        bus.register<GroundCollision> { onGroundCollision() }
        bus.register<PlatformCollision> { onPlatformCollision(it.aabb) }
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
        //handleSpeed and virtual update positions
        //calculate collisions wit other moving objects, ground and platforms
        //determine the type on which we collide
        //call callbacks
    }


    //collision callbacks
    override fun onPlayerCollision(physicsOther: Physics) {
        var hitType: Direction = Direction.NONE
        val whatState = getCurrentState()
        if (whatState == dieState) {
            //do nothing
        } else {
            //hitType = usedForDeterminingTypeOfPlayerCollision(physics, physicsOther)
        }

        when(hitType) {
            Direction.DOWN -> {
                velocity.y = 0.1
            }
            Direction.UP -> {
                velocity.y = -0.1
            }
            Direction.RIGHT -> {
                velocity.x = 0.1
            }
            Direction.LEFT -> {
                velocity.x = -0.1
            }
            else -> {

            }
        }

    }

    override fun onEnemyCollision(physicsOther: Physics) {
        onPlayerCollision(physicsOther)
    }

    override fun onGroundCollision() {
        velocity.y = -0.1
    }

    override fun onPlatformCollision(platform: Physics) {
        //val hitType: Direction = usedForDeterminingTypeOfPlayerCollision(physics, platform)
        /*if (hitType == Direction.UP) {
            onGroundCollision()
        }*/
    }

    override fun onNormalAttackCollision(damage: Double) {
        if (getDamageCooldown <= 0) {
            getDamageCooldown = 120
            healthpoints -= damage
            if (healthpoints <= 0) {
                doStateChange(dieState)
            } else {
                doStateChange(getDamageState)
            }
        }
    }

    override fun onRangedAttackCollision(damage: Double) {
        onNormalAttackCollision(damage)
    }

    override fun onSpecialAttackCollision(damage: Double) {
        onNormalAttackCollision(damage)
    }


    /** Here's where the fun begins. Implementing all states (They're currently 9)      */

    override fun beginState_idle() {
        this.timer = 0
        //println("Ich beginne, nichts zu tun")
        model.animation.play("steady") //TODO(Play idle based on direction)
        resetYPhysics()

    }

    override fun executeState_idle(dt: Double) {
        timer += 1
        //println("Ich bin dabei, nichts zu tun")
        calculateCollisions()
       // physics.update(dt)
    }

    override fun endState_idle() { /* Nothing in here */
        //println("Ich höre auf nichts zu tun")
    }

    override fun beginState_walk() {
        resetXPhysics()
        //println("Ich beginne zu laufen")
        timer = 0
        model.animation.play("run") //TODO(Play run based on direction)
    }

    override fun executeState_walk(dt: Double) {
        //println("Ich bin dabei zu laufen")
        timer += 1
        calculateCollisions()
        //physics.update(dt, maxSpeed, xSpeedStep, true)
    }

    override fun endState_walk() {
        //println("Ich höre auf zu laufen")
    }

    override fun beginState_turn() {
        timer = 0
        direction = this.direction xor 1
        model.animation.play("turn_${direction}")
    }

    override fun executeState_turn(dt: Double) {
        timer += 1
        calculateCollisions()
       // physics.update(dt)
        if (model.animation.isCompleted) bus.send(StateTransition(walkState))
    }

    override fun endState_turn() { /* Nothing in here */
    }


    override fun beginState_jump() {
        this.timer = 0
        model.animation.play("win", 1)
        /*if (direction == 1) {
            model.animation.play("jump_right", 1)
        } else if (direction == 0) {
            model.animation.play("jump_left", 1)
        }*/
    }

    override fun executeState_jump(dt: Double) {
        timer += 1
        calculateCollisions()
        //physics.update(dt, maxSpeed * 0.8, xSpeedStep, false)
        if (model.animation.isCompleted) model.animation.play("steady") //TODO(Play idle based on direction)
    }

    override fun endState_jump() { /* Nothing here */
    }


    override fun beginState_die() {
        this.disablePhysics()
        this.timer = 0
        model.animation.play("die", 1)  //TODO(die animation based on direction)
    }

    override fun executeState_die(dt: Double) {
        timer += 1
        if (model.animation.isCompleted) {
            this.kill()
        }
    }

    override fun endState_die() { /* Nothing in here */
    }


    override fun beginState_normalAttack() {
        timer = 0
        model.animation.play("normal_attack", 1)    //TODO(animation based on direction)
    }

    override fun executeState_normalAttack(dt: Double) {
        timer += 1
        //check if he collides with something -> this can take damage
        calculateCollisions()
        //physics.update(dt)
        if (model.animation.isCompleted) {
            bus.send(StateTransition(idleState))
        }
    }

    override fun endState_normalAttack() { /* Nothing in here */
    }


    override fun beginState_rangedAttack() {
        timer = 0
        model.animation.play("ranged_attack", 1)       //TODO(animation based on direction)
        //TODO(create a new flying object -> check for collision -> apply damage)
    }

    override fun executeState_rangedAttack(dt: Double) {
        timer += 1
        calculateCollisions()
        //physics.update(dt)
        if (model.animation.isCompleted) {
            bus.send(StateTransition(idleState))
        }
    }

    override fun endState_rangedAttack() { /* Nothing in here */
    }


    override fun beginState_specialAttack() {
        timer = 0
        model.animation.play("special_attack", 1)   //TODO(animation based on direction)
        //TODO(maybe shoot something or ...)
    }

    override fun executeState_specialAttack(dt: Double) {
        timer += 1
        //no physics update for now
        calculateCollisions()
        if (model.animation.isCompleted) {
            doStateChange(manager.stateStack[manager.stateStack.size - 1])
        }
    }

    override fun endState_specialAttack() { /* Nothing in here */
    }


    override fun beginState_getDamage() {
        timer = 0
        //println("Ich beginne Schaden zu erhalten")
        model.animation.play("win", 1)  //TODO(correct animation based on direction)
        //model.animation.play("get_damage", 1)
        //play sound
    }

    override fun executeState_getDamage(dt: Double) {
        //println("Ich bin dabei Schaden zu erhalten")
        timer += 1
        calculateCollisions()
        //physics.update(dt)
        if (model.animation.isCompleted) {
            bus.send(StateTransition(idleState))
        }
    }

    override fun endState_getDamage() { /* Nothing in here*/
        //println("Ich höre auf Schaden zu erhalten")
    }

}