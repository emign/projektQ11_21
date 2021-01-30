package actor

import com.soywiz.korma.geom.Point
import eventBus.EventBus
import fsm.StateManager
import fsm.declareState
import fsm.useStates
import kotlinx.coroutines.CoroutineScope
import physic.Physics

/**
 * A MovingActor is an [Actor] which can move on screen
 * @param scope The current scope where the actor is initialized; used for the [EventBus]
 * @param actorXmlData The data read by [readCharacterXmlData]
 */
abstract class MovingActor(val scope: CoroutineScope, val actorXmlData: ActorXmlData): Actor() {

    var velocity: Point = Point(0.0, -1.0 * actorXmlData.jumpHeight)
    var lastVelocity: Point = Point(0.0, 0.0)
    val maxSpeed: Double = actorXmlData.movementSpeed
    val xSpeedStep: Double = 0.2
    val gravity: Double = 0.15

    val actorName: String = actorXmlData.name
    val dbName: String = actorXmlData.dbName
    val skeletonJsonFile: String = actorXmlData.skeletonJsonFile
    val textureJsonFile: String = actorXmlData.textureJsonFile
    val imageFile: String = actorXmlData.imageFile
    val healthpoints: Double = actorXmlData.healthpoints
    var direction: Int = actorXmlData.direction
    val normalAttack: Attack = actorXmlData.normalAttack
    val rangedAttack: Attack = actorXmlData.rangedAttack
    val specialAttack: Attack = actorXmlData.specialAttack

    val bus: EventBus = EventBus(scope)

    var baseLine: Double = this.y + this.height

    abstract val physics: Physics

    /**
     * Initialize all events which are used in this [MovingActor]
     */
    abstract fun initEvents()

    /** Enables physics and apply gravity   */
    abstract fun enablePhysics()
    /** Disable physics and disable gravity     */
    abstract fun disablePhysics()

    //Collision callbacks, triggered by events
    abstract fun onPlayerCollision()
    abstract fun onEnemyCollision()
    abstract fun onGroundCollision()
    abstract fun onPlatformCollision()
    abstract fun onNormalAttackCollision(damage: Double)
    abstract fun onRangedAttackCollision(damage: Double)
    abstract fun onSpecialAttackCollision(damage: Double)

    /** Initilize the [StateManager]        */
    override val manager: StateManager = useStates()

    //init states and pass in the functions for onBegin, onExecute and onEnd
    val idleState = declareState({ beginState_idle() }, { executeState_idle() }, { endState_idle() })
    val walkState = declareState({ beginState_walk() }, { executeState_walk() }, { endState_walk() })
    val turnState = declareState({ beginState_turn() }, { executeState_turn() }, { endState_turn() })
    val jumpState = declareState({ beginState_jump() }, { executeState_jump() }, { endState_jump() })
    val dieState = declareState({ beginState_die() }, { executeState_die() }, { endState_die() })
    val normalAttackState = declareState({ beginState_normalAttack() }, { executeState_normalAttack() }, { endState_normalAttack() })
    val rangedAttackState = declareState({ beginState_rangedAttack() }, { executeState_rangedAttack() }, { endState_rangedAttack() })
    val specialAttackState = declareState({ beginState_specialAttack() }, { executeState_specialAttack() }, { endState_specialAttack() })
    val getDamageState = declareState({ beginState_getDamage() }, { executeState_getDamage() }, { endState_getDamage() })


    /**
     * All state-functions are listed here. They have to be overridden in subclasses
     */
    abstract fun beginState_idle()
    abstract fun executeState_idle()
    abstract fun endState_idle()
    abstract fun beginState_walk()
    abstract fun executeState_walk()
    abstract fun endState_walk()
    abstract fun beginState_turn()
    abstract fun executeState_turn()
    abstract fun endState_turn()
    abstract fun beginState_jump()
    abstract fun executeState_jump()
    abstract fun endState_jump()
    abstract fun beginState_die()
    abstract fun executeState_die()
    abstract fun endState_die()
    abstract fun beginState_normalAttack()
    abstract fun executeState_normalAttack()
    abstract fun endState_normalAttack()
    abstract fun beginState_rangedAttack()
    abstract fun executeState_rangedAttack()
    abstract fun endState_rangedAttack()
    abstract fun beginState_specialAttack()
    abstract fun executeState_specialAttack()
    abstract fun endState_specialAttack()
    abstract fun beginState_getDamage()
    abstract fun executeState_getDamage()
    abstract fun endState_getDamage()
}