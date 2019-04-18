description = "A tool to pretty print/decode clipboard content."
version = "0.2"

plugins {
	java
	application
}

application {
	applicationName = "PrettyPaster"
	mainClassName = "prettypaster.PrettyPaster"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8

	tasks {
		compileTestJava {
			sourceCompatibility = "11"
			targetCompatibility = "11"
		}
	}
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
	implementation("com.google.code.gson:gson:2.8.5")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")
}