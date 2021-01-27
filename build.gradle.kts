import com.soywiz.korge.gradle.*


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

	dependencies {
		add("commonMainApi", "com.soywiz.korlibs.korge:korge-spine:${korgeVersion}")
		add("commonMainApi", "com.soywiz.korlibs.korge:korge-swf:${korgeVersion}")
		add("commonMainApi", "com.soywiz.korlibs.korge:korge-dragonbones:${korgeVersion}")
	}

// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets
	
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
			dependencies{
				implementation("io.ktor:ktor-client-core:1.5.0")
			}
		}

	}
}