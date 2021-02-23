import com.soywiz.korge.gradle.*
import java.util.regex.Pattern.compile

val ktor_version: String by project
val kotlin_version: String by project

buildscript {
    val korgePluginVersion: String by project


    repositories {
        mavenLocal()
        maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
    }
}

plugins {
    id("com.soywiz.korge")
}

korge {
    id = "com.sample.demo"
    supportBox2d()
    supportSpine()
    supportDragonbones()
    supportSwf()

// To enable all targets at once

    //targetAll()

// To enable targets based on properties/environment variables
    //targetDefault()

// To selectively enable targets
    //

    targetJvm()
    targetJs()
    targetDesktop()
    targetIos()
    targetAndroidIndirect() // targetAndroidDirect()
}

kotlin {
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
        }


        val commonMain by getting {
            dependencies {
                //implementation("io.ktor:ktor-client-core:$ktor_version")
                //implementation("io.ktor:ktor-network:$ktor_version")
            }
        }

    }
}