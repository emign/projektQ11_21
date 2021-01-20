package Scenes

import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.color.*
import com.soywiz.korio.file.std.resourcesVfs

class TestScene : Scene() {
    override suspend fun Container.sceneInit() {

        val unserKtree = resourcesVfs["Scene1.ktree"].readKTree(views())
        addChild(unserKtree)

    }
}