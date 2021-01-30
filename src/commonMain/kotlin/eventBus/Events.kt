package eventBus

import actor.actors.Platform
import fsm.StateExecutor
import physic.Physics

class JumpEvent()
class RightEvent()
class LeftEvent()
class IdleEvent()
class GroundedEvent()

//Character important events
class PlayerCollision(val activePhysics: Physics)
class EnemyCollision(val activePhysics: Physics)
class GroundCollision
class PlatformCollision(val platform: Platform)
class NormalAttackCollision(val damage: Double)
class RangedAttackCollision(val damage: Double)
class SpecialAttackCollision(val damage: Double)

//StateEvents
class StateTransition(val state: StateExecutor)