import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.jfrog.artifactory") version "4.5.4"
    id("groovy")
    id("scala")
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"

}

group = "com.demo.myretail"
version = "2.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {

    mavenCentral()
    mavenLocal()

}

extra["springCloudVersion"] = "Hoxton.SR1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation( "org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.codehaus.groovy:groovy-all:2.5.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("io.github.microutils:kotlin-logging:1.7.7")
    implementation("io.springfox:springfox-swagger2:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("io.springfox:springfox-spring-webflux:3.0.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
   testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")
    testImplementation("org.spockframework:spock-spring:1.2-groovy-2.5") {
        exclude( module = "groovy-all")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.github.tomakehurst:wiremock:1.57")
    testImplementation("com.github.tomjankes:wiremock-groovy:0.2.0")
    testImplementation( "junit:junit:4.12")
    testImplementation( "org.cassandraunit:cassandra-unit:3.5.0.1")
    testImplementation( "org.cassandraunit:cassandra-unit-spring:3.5.0.1")
    testImplementation( "io.gatling.highcharts:gatling-charts-highcharts:3.3.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

/*
tasks.withType<Test> {
    useJUnitPlatform()
}
*/

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
