plugins {
    id("java")
}

group = "dp1key.cfs"
version = "1.0.0-beta1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("xerces:xercesImpl:2.12.2")
}

tasks.test {
    useJUnitPlatform()
}