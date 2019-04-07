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
	test {
		java {
			setSrcDirs(listOf("test"))
		}
	}
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}

repositories {
	jcenter()
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")
}