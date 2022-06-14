import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("maven-publish")
    id("signing")
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}


group = "net.peihuan"
version = "0.0.3"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
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

// 去除 jar plain 后缀
tasks.getByName<Jar>("jar") {
    enabled = true
    archiveClassifier.set("")
}

java {
    withSourcesJar()
    withJavadocJar()
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "baidu-pan-starter"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("peihuanhuan/baidu-pan-starter")
                description.set("百度云盘 Java SDK")
                url.set("https://github.com/peihuanhuan/baidu-pan-starter")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("peihuan")
                        name.set("peihuan")
                        email.set("1678167835@qq.com")
                    }
                }
                scm {
                    connection.set("scm:git://github.com/peihuanhuan/baidu-pan-starter.git")
                    developerConnection.set("scm:git://github.com/peihuanhuan/baidu-pan-starter.git")
                    url.set("git://github.com/peihuanhuan/baidu-pan-starter.git")
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = findProperty("ossrhUsername").toString()
                password = findProperty("ossrhPassword").toString()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
