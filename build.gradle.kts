plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

group = "io.stormcat.sandbox"
version = "1.0-SNAPSHOT"

val lombokVersion = "1.18.34"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:9.0.0")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testcontainers:junit-jupiter:1.20.1")
    testImplementation("org.testcontainers:mysql:1.20.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
