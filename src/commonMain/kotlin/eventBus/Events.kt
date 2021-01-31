package eventBus

import actor.actors.Platform
import fsm.StateExecutor
import physic.AABB
import physic.Physics

class JumpEvent()
class RightEvent()
class LeftEvent()
class IdleEvent()
class GroundedEvent()

//Character important events
class PlayerCollision(val aabb: AABB)
class EnemyCollision(val aabb: AABB)
class GroundCollision
class PlatformCollision(val aabb: AABB)
class NormalAttackCollision(val damage: Double)
class RangedAttackCollision(val damage: Double)
class SpecialAttackCollision(val damage: Double)

//StateEvents
class StateTransition(val state: StateExecutor)