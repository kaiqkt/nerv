plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
}

group = "com.kaiqkt"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Jar>("bootJar") {
    archiveFileName.set("app.jar")
}

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.12"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.azam.ulidj:ulidj:1.0.1")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("com.h2database:h2")
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.github.slugify:slugify:3.0.7")
    implementation("com.nimbusds:nimbus-jose-jwt:10.5")

    testImplementation("org.mock-server:mockserver-netty:5.15.0") {
        exclude(group = "org.slf4j", module = "slf4j-jdk14")
    }
    testImplementation("org.mock-server:mockserver-client-java:5.15.0") {
        exclude(group = "org.slf4j", module = "slf4j-jdk14")
    }
    testImplementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val excludePackages: List<String> by extra {
    listOf(
        "com/kaiqkt/nerv/Application*",
        "com/kaiqkt/nerv/application/config/*",
        "com/kaiqkt/nerv/application/web/requests/*",
        "com/kaiqkt/nerv/application/web/responses/*",
        "com/kaiqkt/nerv/domain/models/*",
        "com/kaiqkt/nerv/domain/dtos/*",
        "com/kaiqkt/nerv/utils/*",
    )
}

@Suppress("UNCHECKED_CAST")
fun ignorePackagesForReport(jacocoBase: JacocoReportBase) {
    jacocoBase.classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(jacocoBase.project.extra.get("excludePackages") as List<String>)
        },
    )
}

tasks.withType<JacocoReport> {
    reports {
        html.required.set(true)
    }
    ignorePackagesForReport(this)
}

tasks.withType<JacocoCoverageVerification> {
    violationRules {
        rule {
            limit {
                minimum = "1.0".toBigDecimal()
                counter = "LINE"
            }
            limit {
                minimum = "1.0".toBigDecimal()
                counter = "BRANCH"
            }
        }
    }
    ignorePackagesForReport(this)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}
