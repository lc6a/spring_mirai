plugins {
    java
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    testImplementation(kotlin("test-junit"))
}