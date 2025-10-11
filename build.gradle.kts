plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.0"
}

group = "com.olgo"
version = "0.0.1-SNAPSHOT"

val liquibaseVersion = "4.29.2"     // for liquibase-core + liquibase-hibernate6
val pgDriverVersion = "42.7.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    //JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    //DB - Liquibase
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql")

    // --- Liquibase Gradle plugin runtime (separate classpath for generation tasks) ---
    add("liquibaseRuntime", "org.liquibase:liquibase-core:$liquibaseVersion")
    add("liquibaseRuntime", "org.liquibase.ext:liquibase-hibernate6:$liquibaseVersion")
    add("liquibaseRuntime", "org.postgresql:postgresql:$pgDriverVersion")

    // Make your compiled classes + runtime deps visible to the plugin (so it can see entities)
    add("liquibaseRuntime", sourceSets.main.get().output)
    add("liquibaseRuntime", sourceSets.main.get().runtimeClasspath)
    add("liquibaseRuntime", "info.picocli:picocli:4.7.6")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


// Liquibase plugin configuration
configure<org.liquibase.gradle.LiquibaseExtension> {
    val env = (project.findProperty("env") as String?) ?: "test"

    activities.register("main") {
        val changeLogPath = "src/main/resources/db/changelog/changes/001-initial.yaml"
        val dbUrl = if (env == "test")
            "jdbc:postgresql://localhost:5432/cookbook_db"
        else
            "jdbc:postgresql://localhost:5432/cookbook_db"

        val dbUser = if (env == "test") "cookbook_dba" else "cookbook_dba"
        val dbPass = if (env == "test") (System.getenv("DB_PASSWORD") ?: "") else (System.getenv("DB_PASSWORD") ?: "")



        arguments = mapOf(
            "changelogFile" to changeLogPath,
            "url" to dbUrl,
            "username" to dbUser,
            "password" to dbPass,

            // IMPORTANT: set to your entity base package (adjust if needed)
            "referenceUrl" to
                    "hibernate:spring:com.olgo.cookbook" +
                    "?dialect=org.hibernate.dialect.PostgreSQLDialect" +
                    "&hibernate.physical_naming_strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        )
    }

    runList = "main"
}

