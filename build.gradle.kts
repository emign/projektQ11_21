import com.soywiz.korge.gradle.*
import java.util.regex.Pattern.compile


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

apply(plugin = "korge")

korge {
	id = "com.sample.demo"

	dependencies {
		add("commonMainApi", "com.soywiz.korlibs.korge:korge-spine:${korgeVersion}")
		add("commonMainApi", "com.soywiz.korlibs.korge:korge-swf:${korgeVersion}")
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
