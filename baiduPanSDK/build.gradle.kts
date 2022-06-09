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
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()
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

val uploadArchives: Upload by tasks
uploadArchives.apply {
    repositories {
        withConvention(MavenRepositoryHandlerConvention::class) {
            mavenDeployer {
                // Sign Maven POM
                beforeDeployment {
                    signing.signPom(this)
                }

                val username = if (project.hasProperty("sonatypeUsername")) project.properties["sonatypeUsername"] else System.getenv("sonatypeUsername")
                val password = if (project.hasProperty("sonatypePassword")) project.properties["sonatypePassword"] else System.getenv("sonatypePassword")

                withGroovyBuilder {
                    "snapshotRepository"("url" to "https://oss.sonatype.org/content/repositories/snapshots") {
                        "authentication"("userName" to username, "password" to password)
                    }

                    "repository"("url" to "https://oss.sonatype.org/service/local/staging/deploy/maven2") {
                        "authentication"("userName" to username, "password" to password)
                    }
                }

                // Maven POM generation
                pom.project {
                    withGroovyBuilder {
                        "name"(projectName)
                        "artifactId"(base.archivesBaseName.toLowerCase())
                        "packaging"("jar")
                        "url"("https://github.com/OpenCubicChunks/CubicChunksConverter")
                        "description"("Save converter from vanilla Minecraft to cubic chunks")


                        "scm" {
                            "connection"("scm:git:git://github.com/OpenCubicChunks/CubicChunksConverter.git")
                            "developerConnection"("scm:git:ssh://git@github.com:OpenCubicChunks/CubicChunksConverter.git")
                            "url"("https://github.com/OpenCubicChunks/CubicChunksConverter")
                        }

                        "licenses" {
                            "license" {
                                "name"("The MIT License")
                                "url"("http://www.tldrlegal.com/license/mit-license")
                                "distribution"("repo")
                            }
                        }

                        "developers" {
                            "developer" {
                                "id"("Barteks2x")
                                "name"("Barteks2x")
                            }
                        }

                        "issueManagement" {
                            "system"("github")
                            "url"("https://github.com/OpenCubicChunks/CubicChunksConverter/issues")
                        }
                    }
                }
            }
        }
    }
}

tasks {

    // TODO prefer the lazy string invoke once https://github.com/gradle/gradle-native/issues/718 is fixed
    getByName<Upload>("uploadArchives") {

        repositories {

            withConvention(MavenRepositoryHandlerConvention::class) {

                mavenDeployer {

                    withGroovyBuilder {
                        "repository"("url" to uri("$buildDir/m2/releases"))
                        "snapshotRepository"("url" to uri("$buildDir/m2/snapshots"))
                    }

                    pom.project {
                        withGroovyBuilder {
                            "parent" {
                                "groupId"("org.gradle")
                                "artifactId"("kotlin-dsl")
                                "version"("1.0")
                            }
                            "licenses" {
                                "license" {
                                    "name"("The Apache Software License, Version 2.0")
                                    "url"("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                    "distribution"("repo")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
