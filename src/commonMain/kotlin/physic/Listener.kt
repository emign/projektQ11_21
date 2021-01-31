package physic

class Listener(val rectangles: List<AABB>) {

    val THRESHOLD: Double = 0.02

    enum class PointType(val number: Int) { BEGIN(0), END(1) }
    class EndPoint(val type: PointType, val value: Double, val aabbIndex: Int)
    class Pair(val r1: AABB, val r2: AABB)

    var xEndPoints: MutableList<EndPoint> = mutableListOf()


    fun update(): MutableList<Pair> {
        xEndPoints = mutableListOf()
        for (i in rectangles.indices) {
            xEndPoints.add(2 * i, EndPoint(PointType.BEGIN, rectangles[i].left, i))
            xEndPoints.add(2 * i + 1, EndPoint(PointType.END, rectangles[i].right, i))
        }
        xEndPoints = insertionSort(xEndPoints)
        return updateY(updateX())
    }

    fun updateX(): MutableList<Pair> {
        xEndPoints = insertionSort(xEndPoints)
        val activeList = mutableListOf<EndPoint>()
        val collisions = mutableListOf<Pair>()
        for (i in 0 until xEndPoints.size - 1) {
            if (xEndPoints[i].type == PointType.BEGIN) {
                activeList.add(xEndPoints[i])
            } else {
                if (xEndPoints[i + 1].value - xEndPoints[i].value < THRESHOLD) {
                    collisions.add(Pair(rectangles[xEndPoints[i].aabbIndex], rectangles[xEndPoints[i + 1].aabbIndex]))
                }
                activeList.remove(activeList.filter { it.aabbIndex == xEndPoints[i].aabbIndex }[0])
                activeList.forEach {
                    collisions.add(Pair(rectangles[xEndPoints[i].aabbIndex], rectangles[it.aabbIndex]))
                }
            }
        }
        return collisions
    }

    fun updateY(collidables: MutableList<Pair>): MutableList<Pair> {
        val new = mutableListOf<Pair>()
        collidables.forEach { pair ->
            if (pair.r1.top > pair.r2.bottom || pair.r2.top > pair.r1.bottom) {
                //collisions.remove(pair)
            } else {
                new.add(pair)
            }
        }
        return new
    }

    fun insertionSort(items: MutableList<EndPoint>): MutableList<EndPoint> {
        if (items.isEmpty() || items.size < 2) {
            return items
        }
        for (count in 1 until items.size) {
            val item = items[count]
            var i = count
            while (i > 0 && item.value < items[i - 1].value) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }
        return items
    }

}