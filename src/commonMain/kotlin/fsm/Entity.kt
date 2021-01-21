package fsm

import com.esotericsoftware.spine.*
import com.esotericsoftware.spine.korge.SkeletonView
import com.esotericsoftware.spine.korge.skeletonView
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.Atlas
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs

class daTestEnemy(val skeletonData: SkeletonData) {

    companion object {
        suspend fun build(skeletonFile: String, atlasFile: String, scale: Float): daTestEnemy {
            val atlas = resourcesVfs[atlasFile].readAtlas(true)
            val skeletonData = resourcesVfs[skeletonFile].readSkeletonBinary(atlas, scale)
            return daTestEnemy(skeletonData)
        }
    }

    val wrapper = bindSceletonAndAnimations(skeletonData)
    val model = wrapper.model
    val state = wrapper.state
    val modelView = SkeletonView(model, state)
    val healthpoints: Int = 5
    var timer: Int = 0
    val movementSpeed: Double = 5.0
    val jumpHeight: Double = 70.0
    val baseLine = modelView.y
    var isAtMaxJumpHeight: Boolean = false

    val stateSystem = createStateBase()
    val jumpState = stateSystem.createState(this)
    val runState = stateSystem.createState(this)
    val deathState = stateSystem.createState(this)

    data class Wrapper(val state: AnimationState, val model: Skeleton)
    //fun collisionCallback(other: daTestEnemy)

    init {
        onCreate()
        modelView.addUpdater {
            onExecute()
        }
    }

    fun bindSceletonAndAnimations(data: SkeletonData): Wrapper {
        val skeleton = Skeleton(skeletonData)
        val stateData = AnimationStateData(skeletonData)

        //set mixes for all animation transitions
        stateData.setMix("run", "jump", 0.2f)
        stateData.setMix("idle", "run", 0.2f)
        stateData.setMix("jump", "run", 0.2f)
        stateData.setMix("run", "death", 0.2f)
        stateData.setMix("run", "run", 0.25f)
        stateData.setMix("death", "run", 0.2f)
        stateData.setMix("run", "shoot", 0.2f)
        stateData.setMix("shoot", "hoverboard", 0.5f)

        //set actual state and timeScale
        val state = AnimationState(stateData)
        state.timeScale = 1f

        state.setAnimation(0, "idle", true)

        state.update(1.0f/60.0f)

        state.apply(skeleton)
        skeleton.updateWorldTransform()

        return Wrapper(state, skeleton)
    }

    fun onCreate() {
        initStates()
        stateSystem.setStartState(runState)
    }

    fun onExecute() {
        if (modelView.x >= 1300) spriteCollision()
        stateSystem.update()
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
    }


    fun spriteCollision() {
        if (stateSystem.getCurrentState() == runState) {
            stateSystem.doStateChange(deathState)
        }
    }


    fun beginState_run() {
        state.setAnimation(0, "run", true)
    }

    fun executeState_run() {
        this.modelView.x += 5
        if(this.healthpoints <= 0) stateSystem.doStateChange(deathState)
    }

    fun endState_run() {

    }

    fun beginState_death() {
        state.setAnimation(0, "death", false)
    }

    fun executeState_death() {
        this.timer += 1
        if (this.timer > 450) this.modelView.removeFromParent()

    }

    fun endState_death() {

    }

    fun beginState_jump() {
        state.setAnimation(0, "jump", false)
    }

    fun executeState_jump() {
        if (isAtMaxJumpHeight) {
            if (baseLine - modelView.y > 0) modelView.y += movementSpeed
            else {
                stateSystem.doStateChange(runState)
                isAtMaxJumpHeight = false
            }
        } else {
            if (baseLine - modelView.y >= jumpHeight) isAtMaxJumpHeight = true
            else modelView.y -= movementSpeed
        }
    }

    fun endState_jump() {

    }
}
