plugins {
	id("com.soywiz.korge")
}

korge {
	id = "com.sample.demo"

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



