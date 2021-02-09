import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

val miraiVersion = "2.3.2"
val devVersion = "1"

group = "com.lc"
version = "${miraiVersion}-dev${devVersion}"

java.sourceCompatibility = JavaVersion.VERSION_15

repositories {
    mavenLocal()
    maven (url = "http://maven.aliyun.com/nexus/content/groups/public/")
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    implementation("net.mamoe:mirai-core-jvm:${miraiVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "15"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
