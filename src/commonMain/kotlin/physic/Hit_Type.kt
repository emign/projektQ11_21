package physic

import com.soywiz.korge.view.*

abstract class Hit_Type() {

    abstract val view : View?

    class Player(override val view: View?=null):Hit_Type()
    class Bullet(override val view: View?=null):Hit_Type()
    class Ground(override val view: View?=null):Hit_Type()
    class Platform(override val view: View?=null):Hit_Type()
}