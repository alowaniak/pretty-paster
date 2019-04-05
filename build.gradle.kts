description = "A tool to pretty print/decode clipboard content."
version = "0.1"

plugins {
	java
	application
}

application {
	mainClassName = "prettypaster.PrettyPaster"
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
	main {
		java {
			setSrcDirs(listOf("src"))
		}
	}
}

repositories {
	jcenter()
}

