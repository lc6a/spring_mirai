import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Version{
    /**
     * mirai版本
     */
    const val mirai = "2.11.1"

    /**
     * spring_mirai开发版本
     */
    const val dev = "1"

    const val kotlin = "1.7.0"

    const val spring_boot = "2.7.0"
}

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
}


group = "com.lc"
version = "${Version.mirai}-dev${Version.dev}"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    maven (url = "https://maven.aliyun.com/nexus/content/groups/public/")
    maven (url = "https://plugins.gradle.org/m2/")
    mavenCentral()
    jcenter()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    // 为了开箱即用，不使用mysql数据库了, 用plugin data保存数据
    //implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.3")
    //implementation("mysql:mysql-connector-java:8.0.22")
    implementation("net.mamoe:mirai-console-terminal:${Version.mirai}") // 自行替换版本
    implementation("net.mamoe:mirai-core-jvm:${Version.mirai}") {
        exclude("net.mamoe", "mirai-core-api")
        exclude("net.mamoe", "mirai-core-utils")
    }
    implementation("net.mamoe:mirai-core-api-jvm:${Version.mirai}") {
        exclude("net.mamoe", "mirai-core-utils")
    }
    implementation("net.mamoe:mirai-core-utils-jvm:${Version.mirai}")
    implementation("net.mamoe:mirai-console:${Version.mirai}")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation("org.ow2.asm:asm-util:9.1")
    implementation("com.auth0:java-jwt:3.10.3")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
