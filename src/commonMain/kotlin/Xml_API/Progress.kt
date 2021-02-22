package Xml_API

import com.soywiz.korge.resources.resourceBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.xml.Xml
import com.soywiz.korio.serialization.xml.readXml

interface XmlData {
    fun getData() : Map<String, Any>
}

enum class XmlType {
    CHARACTER, PROGRESS
}

data class Progress(

    val levelsFinished : Int,
    val skinsUnlocked : List<String>,
    val enemiesKilled : Int,
    val playTime : Double

) : XmlData {
    override fun getData(): Map<String, Any> {
        val map : MutableMap<String, Any> = mutableMapOf()

        map.put("levelsFinished", levelsFinished)
        map.put("enemiesKilled", enemiesKilled)
        map.put("playTime", playTime)
        map.put("skinsUnlocked", skinsUnlocked)
        return map
    }
}
