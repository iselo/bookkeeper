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
        "org.springframework.boot:spring-boot-starter-jdbc",
        "org.springframework.boot:spring-boot-starter-data-jdbc",
        "com.google.guava:guava:33.4.0-jre",
    ).forEach { implementation(it) }

    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    setOf(
        "org.springframework.boot:spring-boot-starter-test",
        "com.google.truth:truth:1.4.4"
    ).forEach { testImplementation(it) };

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
    }

    // Excluded because SpringBoot application violates `HideUtilityClassConstructor` rule
    checkstyleMain {
        exclude("**/BookkeeperApp.java")
    }
}
