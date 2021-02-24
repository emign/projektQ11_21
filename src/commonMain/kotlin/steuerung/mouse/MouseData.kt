package steuerung.mouse

class MouseData(
    val mouseX: Int,
    val mouseY: Int,
    val mouseLeft: Boolean,
    val mouseRight: Boolean
        ){
    fun equalsData(other: MouseData): Boolean {
        return this.mouseX==other.mouseX&&this.mouseY==other.mouseY&&this.mouseLeft==other.mouseLeft&&this.mouseRight==other.mouseRight
}}