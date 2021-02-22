import com.soywiz.korge.Korge

suspend fun main() {
    val data = XmlReadWrite.read("Characters/Demon.xml", XmlType.CHARACTER)
    XmlReadWrite.write(data, "Characters/Demon2.xml", XmlType.CHARACTER)
}