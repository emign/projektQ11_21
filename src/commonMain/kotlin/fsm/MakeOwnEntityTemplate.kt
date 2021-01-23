package fsm

import com.esotericsoftware.spine.*
import com.esotericsoftware.spine.korge.SkeletonView
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korio.file.std.resourcesVfs

class MakeOwnEntityTemplate(val skeletonData: SkeletonData) {

    companion object {
        /**
         * create a new object of this class -> Better than direct initialization via constructor
         */
        suspend fun build(skeletonFile: String, atlasFile: String, scale: Float): MakeOwnEntityTemplate {
            val atlas = resourcesVfs[atlasFile].readAtlas(true)
            val skeletonData = resourcesVfs[skeletonFile].readSkeletonBinary(atlas, scale)
            return MakeOwnEntityTemplate(skeletonData)
        }
    }


    //Spine model and animations
    var bodyModel: Skeleton = Skeleton(skeletonData)
    var animationState: AnimationState = bindSkeletonAnimations(skeletonData, bodyModel)
    val modelView = SkeletonView(bodyModel, animationState)

    //Entity specific variables
    val healthpoints: Int = 5
    var timer: Int = 0
    val movementSpeed: Double = 5.0
    val jumpHeight: Double = 70.0
    val baseLine = modelView.y
    var isAtMaxJumpHeight: Boolean = false
    var dead: Boolean = false

    //create states
    val stateManager = createStateManager()
    val jumpState = createState("jump", stateManager)
    val runState = createState("run", stateManager)
    val deathState = createState("death", stateManager)
    val idleState = createState("idle", stateManager)


    //what should be done on initialization?
    init {

        //set things up ...
        onCreate()

        //do every frame -> loop of the Entity
        modelView.addUpdater {
            onExecute()
        }
    }


    /**
     * Adds animations contained in [data] to the parameter [skeleton]
     */
    fun bindSkeletonAnimations(data: SkeletonData, skeleton: Skeleton): AnimationState {
        val stateData = AnimationStateData(data)

        //set mixes for all animation transitions
        stateData.setMix("run", "jump", 0.4f)
        stateData.setMix("idle", "run", 0.2f)
        stateData.setMix("jump", "run", 0.2f)
        stateData.setMix("run", "death", 0.2f)
        stateData.setMix("run", "run", 0.25f)
        stateData.setMix("death", "run", 0.2f)
        stateData.setMix("run", "shoot", 0.2f)
        stateData.setMix("run", "idle", 0.2f)
        stateData.setMix("shoot", "hoverboard", 0.5f)
        stateData.setMix("jump", "idle", 0.5f)

        //set actual state and timeScale
        val state = AnimationState(stateData)
        state.timeScale = 1f

        state.setAnimation(0, "idle", true)

        state.update(1.0f / 60.0f)

        state.apply(skeleton)
        skeleton.updateWorldTransform()

        return state
    }


    /**
     * Setup function which should be called once after creating a new [MakeOwnEntityTemplate]
     * This should initialize the states and set a start state
     * Maybe it can initialize even more in the future... but for now, just the states
     */
    fun onCreate() {
        initStates()
        stateManager.setStartState(idleState)
    }

    /**
     * Called every frame -> main loop, should update the currentState and to maybe even more later on
     */
    fun onExecute() {
        if (modelView.x >= 1300) spriteCollision()
        stateManager.updateCurrentState()
    }

    /**
     * Deletes the entity after it is dead
     */
    fun delete() {
        this.modelView.removeFromParent()
        //if the entity is dead, it should not be updated any more by the Updater
        dead = true
        println("ENNNND")
    }

    /**
     * Initialize all states. Add code to the states. Each state has the following actions:
     * onBegin -> called every time the entity enters this state -> setup
     * onExecute -> called every frame if the state is the entity's current State
     * onEnd -> called if the entity leaves the state -> called once at the end to clear up
     */
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

        idleState.onBegin { animationState.setAnimation(0, "idle", true) }
    }


    //Simple testing function
    fun spriteCollision() {
        if (stateManager.getCurrentState() == runState) {
            stateManager.doStateChange(deathState)
        }
    }


    //Fun begins -> implement each state
    fun beginState_run() {
        animationState.setAnimation(0, "run", true)
    }

    fun executeState_run() {
        this.modelView.x += 5
        if (this.healthpoints <= 0) stateManager.doStateChange(deathState)
    }

    fun endState_run() {

    }

    fun beginState_death() {
        animationState.setAnimation(0, "death", false)
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
        animationState.setAnimation(0, "jump", false)
    }

    fun executeState_jump() {
        if (isAtMaxJumpHeight) {
            if (baseLine - modelView.y > 0) modelView.y += movementSpeed
            else {
                stateManager.doStateChange(idleState)
                isAtMaxJumpHeight = false
            }
        } else {
            if (baseLine - modelView.y >= jumpHeight) isAtMaxJumpHeight = true
            else modelView.y -= movementSpeed
        }
    }

    fun endState_jump() {

    }

    //We can add even more states, as much as we want, e.g. hit, turn, getDamage, ...
}
