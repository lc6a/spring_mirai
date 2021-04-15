import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Version{
    /**
     * mirai版本
     */
    const val mirai = "2.5.2"

    /**
     * spring_mirai开发版本
     */
    const val dev = "1"
}

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("net.mamoe.mirai-console") version "2.5.2" //尝试从Version.mirai获取失败了
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}


group = "com.lc"
version = "${Version.mirai}-dev${Version.dev}"

java.sourceCompatibility = JavaVersion.VERSION_15
java.targetCompatibility = JavaVersion.VERSION_15

repositories {
    mavenLocal()
    maven (url = "http://maven.aliyun.com/nexus/content/groups/public/")
    mavenCentral()
    jcenter()
}

mirai {

}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.3")
    implementation("mysql:mysql-connector-java:8.0.22")
    implementation("net.mamoe:mirai-console-terminal:${Version.mirai}") // 自行替换版本
    implementation("net.mamoe:mirai-core-jvm:${Version.mirai}") {
        exclude("net.mamoe", "mirai-core-api")
        exclude("net.mamoe", "mirai-core-utils")
    }
    implementation("net.mamoe:mirai-core-api-jvm:${Version.mirai}") {
        exclude("net.mamoe", "mirai-core-utils")
    }
    implementation("net.mamoe:mirai-core-utils-jvm:${Version.mirai}")
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
