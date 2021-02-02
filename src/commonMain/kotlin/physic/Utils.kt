package physic

import org.jbox2d.common.Vec2

operator fun Vec2.plus(new: Vec2): Vec2 {
    return Vec2(x + new.x, y + new.y)
}

operator fun Vec2.times(value: Number): Vec2 {
    return Vec2(this.x * value.toFloat(), this.y * value.toFloat())
}

operator fun Vec2.times(new: Vec2): Float {
    return this.x * new.x + this.y * new.y
}

operator fun Vec2.minus(new: Vec2): Vec2 {
    return Vec2(this.x - new.x, this.y - new.y)
}

fun Vec2.getTangent(): Vec2 {
    return Vec2(-this.y, this.x)
}