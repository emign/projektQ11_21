import com.soywiz.korge.gradle.*

val ktor_version : String by project

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
	
	targetJvm()
	//targetJs()
	//targetDesktop()
	//targetIos()
	//targetAndroidIndirect() // targetAndroidDirect()
}

kotlin {
	sourceSets {
		all {
			languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
			languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
		}


		val commonMain by getting {
			dependencies{
				implementation("io.ktor:ktor-client-core:$ktor_version")
				implementation("io.ktor:ktor-server-core:$ktor_version")
				implementation("io.ktor:ktor-websockets:$ktor_version")

			}
		}

		val jvmMain by getting{
			dependencies{
				implementation("io.ktor:ktor-client-cio:$ktor_version")
				implementation("io.ktor:ktor-server-cio:$ktor_version")
			}
		}

	}
}