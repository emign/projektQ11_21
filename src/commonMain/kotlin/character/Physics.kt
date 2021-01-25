package character

class Physics(var characterBase: CharacterBase) {

    var isActive: Boolean = true

    fun timeStep() {
        if (isActive) {
            //bla bla bla
            //update characterbase position, speed etc.
        }
    }

    fun isPlayerCollision(): Boolean {
        return true
    }

    fun isSpriteCollision(): Boolean {
        return true
    }
}