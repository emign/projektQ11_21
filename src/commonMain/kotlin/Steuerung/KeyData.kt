package Steuerung

class KeyData(val jump:Boolean,
              val left: Boolean,
              val right:Boolean,
              val standardAttack :Boolean,
              val superAttack: Boolean,
              val  rangedAttack: Boolean,
              val block: Boolean) {

    fun equalsData(other: KeyData): Boolean {
        return this.jump==other.jump&&this.left==other.left&&this.right==other.right&&this.standardAttack==other.standardAttack&&this.superAttack==other.superAttack&&this.rangedAttack==other.rangedAttack&&this.block==other.block
    }

}