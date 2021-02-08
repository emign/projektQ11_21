package eventBus

import fsm.StateExecutor
import physic.Physics

class JumpEvent()
class RightEvent()
class LeftEvent()
class IdleEvent()
class GroundedEvent()

//Character important events
class PlayerCollision(val aabb: Physics)
class EnemyCollision(val aabb: Physics)
class GroundCollision
class PlatformCollision(val aabb: Physics)
class NormalAttackCollision(val damage: Double)
class RangedAttackCollision(val damage: Double)
class SpecialAttackCollision(val damage: Double)

//StateEvents
class StateTransition(val state: StateExecutor)