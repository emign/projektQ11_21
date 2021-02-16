package physic.internal

//Just a little bit math :)

import com.soywiz.korma.geom.Rectangle
import org.jbox2d.common.Vec2
import kotlin.math.abs

/**
 * Adds two [Vec2] and returns the result
 */
operator fun Vec2.plus(new: Vec2): Vec2 {
    return Vec2(x + new.x, y + new.y)
}

/**
 * Multiplies a [Vec2] by a scalar value and returns a new vector
 */
operator fun Vec2.times(value: Number): Vec2 {
    return Vec2(this.x * value.toFloat(), this.y * value.toFloat())
}

/**
 * Dot-product of two [Vec2]s. Returns a scalar value
 */
operator fun Vec2.times(new: Vec2): Float {
    return this.x * new.x + this.y * new.y
}

/**
 * Subtracts two [Vec2] and returns the result
 */
operator fun Vec2.minus(new: Vec2): Vec2 {
    return Vec2(this.x - new.x, this.y - new.y)
}

/**
 * Get the tangent of this [Vec2]. This is basically a vector who is orthogonal to the original one.
 * @return new [Vec2]
 */
fun Vec2.getTangent(): Vec2 {
    return Vec2(-this.y, this.x)
}

fun clamp(min: Number, max: Number, value: Number): Float {
    return if (value.toFloat() < min.toFloat()) min.toFloat() else if (value.toFloat() > max.toFloat()) max.toFloat() else value.toFloat()
}


/**
 * Calculates the closest point on a [Rectangle]s bounds to a specific point
 * @param point The point where we want to calculate the smallest distance to the rectangle
 */
internal fun Rectangle.closestPointOnBoundsToPoint(point: Vec2): Vec2 {
    var minDist = abs(point.x - this.x.toFloat())
    var boundsPoint = Vec2(-minDist, 0.0f)
    if (abs(this.x + this.width - point.x) <= minDist) {
        minDist = abs(this.x + this.width - point.x).toFloat()
        boundsPoint = Vec2(minDist, 0.0f)
    }
    if (abs(this.y + this.height - point.y) <= minDist) {
        minDist = abs(this.y + this.height - point.y).toFloat()
        boundsPoint = Vec2(0.0f, minDist)
    }
    if (abs(this.y - point.y) <= minDist) {
        minDist = abs(this.y.toFloat() - point.y)
        boundsPoint = Vec2(0.0f, -minDist)
    }
    return boundsPoint
}