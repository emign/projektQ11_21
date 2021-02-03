package eventBus

import character.Physics
import multiplayer.MultiplayerData

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

//MultiplayerEvents to Support MP
class Multiplayer_StartClient()
class Multiplayer_StartServer()
class Multiplayer_Stop()
class Multiplayer_Send(data:String)
class Multiplayer_Get(data:MultiplayerData)