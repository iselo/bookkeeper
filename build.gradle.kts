plugins {
    java
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.7"
    checkstyle
    jacoco
}

group = "co.raccoons"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    setOf(
        "org.springframework.boot:spring-boot-starter-web",
        "org.springframework.boot:spring-boot-starter-validation",
        "org.springframework.boot:spring-boot-starter-data-mongodb",
        "org.springframework.boot:spring-boot-starter-webflux",
        "org.springframework:spring-aspects",
//        "org.springframework.boot:spring-boot-starter-security",
        "com.papertrailapp:logback-syslog4j:1.0.0",
        "com.google.guava:guava:33.4.0-jre",
    ).forEach { implementation(it) }

    setOf(
        "org.springframework.boot:spring-boot-starter-test",
//        "org.springframework.security:spring-security-test"
    ).forEach { testImplementation(it) }

    annotationProcessor("org.projectlombok:lombok")

    compileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

checkstyle {
    toolVersion = "10.12.4"
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true) // Codecov dependency
        }
    }

    // Excluded because SpringBoot application violates `HideUtilityClassConstructor` rule
    checkstyleMain {
        exclude("**/BookkeeperApp.java")
    }
}
