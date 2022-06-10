import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("maven-publish")

    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}


group = "net.peihuan"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("com.squareup.okhttp3:okhttp:4.10.0-RC1")

    implementation("com.google.code.gson:gson:2.9.0")

    implementation("io.github.microutils:kotlin-logging:2.0.11")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}



java {
    withSourcesJar()
    withJavadocJar()
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "net.peihuan"
            artifactId = "baidu-pan-sdk"
            from(components["kotlin"])

        }
    }
}
