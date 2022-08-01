import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.13"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.springdoc.openapi-gradle-plugin") version "1.3.4"
    val kotlinVersion = "1.6.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.mockk:mockk:1.10.3")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("mysql:mysql-connector-java")
    implementation("com.querydsl:querydsl-jpa")
    implementation("com.querydsl:querydsl-apt")
    kapt("com.querydsl:querydsl-apt:4.2.2:jpa")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.8")
    runtimeOnly("org.springdoc:springdoc-openapi-kotlin:1.6.8")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allOpen {
    val packageName = "javax.persistence."
    annotation("${packageName}Entity")
    annotation("${packageName}MappedSuperclass")
}

openApi {
    apiDocsUrl.set("http://localhost:8080/library-service/v3/api-docs.yaml")
    outputDir.set(file("./doc"))
    outputFileName.set("library-service.yaml")
    waitTimeInSeconds.set(90)
}
