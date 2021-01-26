package eventBus

import character.physic.Physics

class JumpEvent()
class RightEvent()
class LeftEvent()
class IdleEvent()
class GroundedEvent()

//Character important events
class PlayerCollision(val activePhysics: Physics)
class SpriteCollision(val activePhysics: Physics)
class NormalAttackCollision(val damage: Double)
class RangedAttackCollision(val damage: Double)
class SpecialAttackCollision(val damage: Double)