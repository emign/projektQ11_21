package actor

import com.soywiz.korge.dragonbones.KorgeDbArmatureDisplay
import com.soywiz.korge.view.Container
import eventController.*
import fsm.StateManager
import fsm.StateUser
import fsm.declareState
import fsm.useStates
import kotlinx.coroutines.CoroutineScope
import physic.internal.Physics

/**
 * A MovingActor is an [Actor] which can move on screen and have states
 * @param scope The current scope where the actor is initialized; used for the [EventBus]
 * @param actorXmlData The data read by [readCharacterXmlData]
 */
abstract class MovingActor(parent: Container, val scope: CoroutineScope, val actorXmlData: ActorXmlData): Actor(parent), StateUser {

    val maxSpeed: Double = actorXmlData.movementSpeed
    val xSpeedStep: Double = 0.2
    val gravity: Double = 0.15

    val actorName: String = actorXmlData.name
    val dbName: String = actorXmlData.dbName
    val skeletonJsonFile: String = actorXmlData.skeletonJsonFile
    val textureJsonFile: String = actorXmlData.textureJsonFile
    val imageFile: String = actorXmlData.imageFile
    var healthpoints: Double = actorXmlData.healthpoints
    var direction: Int = 1
    val normalAttack: Attack = actorXmlData.normalAttack
    val rangedAttack: Attack = actorXmlData.rangedAttack
    val specialAttack: Attack = actorXmlData.specialAttack

    var getDamageCooldown: Int = 120

    var baseLine: Double = this.y + this.height

    abstract val model: KorgeDbArmatureDisplay

    /**
     * Initialize all events which are used in this [MovingActor]
     */
    abstract fun initEvents()

    /** Enables physic.getPhysics and apply gravity   */
    abstract fun enablePhysics()
    /** Disable physic.getPhysics and disable gravity     */
    abstract fun disablePhysics()

    fun resetYPhysics() {
        velocity.y = 0.0
        lastVelocity.y = 0.0
        lastPosition.y = position.y
    }

    fun resetXPhysics() {
        velocity.x = 0.0
        lastVelocity.x = 0.0
        lastPosition.x = position.x
    }

    fun setAnimation(name: String, times: Int, ) {
        model.animation.play(name, times)
    }

    //Collision callbacks, triggered by events
    abstract fun onPlayerCollision(physicsOther: Physics)
    abstract fun onEnemyCollision(physicsOther: Physics)
    abstract fun onGroundCollision()
    abstract fun onPlatformCollision(platform: Physics)
    abstract fun onNormalAttackCollision(damage: Double)
    abstract fun onRangedAttackCollision(damage: Double)
    abstract fun onSpecialAttackCollision(damage: Double)

    /** Initilize the [StateManager]        */
    override val manager: StateManager = useStates()

    //init states and pass in the functions for onBegin, onExecute and onEnd
    val idleState = declareState({ beginState_idle() }, { executeState_idle(it) }, { endState_idle() })
    val walkState = declareState({ beginState_walk() }, { executeState_walk(it) }, { endState_walk() })
    val turnState = declareState({ beginState_turn() }, { executeState_turn(it) }, { endState_turn() })
    val jumpState = declareState({ beginState_jump() }, { executeState_jump(it) }, { endState_jump() })
    val dieState = declareState({ beginState_die() }, { executeState_die(it) }, { endState_die() })
    val normalAttackState = declareState({ beginState_normalAttack() }, { executeState_normalAttack(it) }, { endState_normalAttack() })
    val rangedAttackState = declareState({ beginState_rangedAttack() }, { executeState_rangedAttack(it) }, { endState_rangedAttack() })
    val specialAttackState = declareState({ beginState_specialAttack() }, { executeState_specialAttack(it) }, { endState_specialAttack() })
    val getDamageState = declareState({ beginState_getDamage() }, { executeState_getDamage(it) }, { endState_getDamage() })


    /**
     * All state-functions are listed here. They have to be overridden in subclasses
     */
    abstract fun beginState_idle()
    abstract fun executeState_idle(dt: Double)
    abstract fun endState_idle()
    abstract fun beginState_walk()
    abstract fun executeState_walk(dt: Double)
    abstract fun endState_walk()
    abstract fun beginState_turn()
    abstract fun executeState_turn(dt: Double)
    abstract fun endState_turn()
    abstract fun beginState_jump()
    abstract fun executeState_jump(dt: Double)
    abstract fun endState_jump()
    abstract fun beginState_die()
    abstract fun executeState_die(dt: Double)
    abstract fun endState_die()
    abstract fun beginState_normalAttack()
    abstract fun executeState_normalAttack(dt: Double)
    abstract fun endState_normalAttack()
    abstract fun beginState_rangedAttack()
    abstract fun executeState_rangedAttack(dt: Double)
    abstract fun endState_rangedAttack()
    abstract fun beginState_specialAttack()
    abstract fun executeState_specialAttack(dt: Double)
    abstract fun endState_specialAttack()
    abstract fun beginState_getDamage()
    abstract fun executeState_getDamage(dt: Double)
    abstract fun endState_getDamage()
}
