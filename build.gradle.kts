val VERSION_PAPER="26.2"
val VERSION_BUILD="+"
val VERSION_HIKARI="7.1.0"

plugins {
    id("java")
}

group = "com.zabrek"
version = VERSION_PAPER

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${VERSION_PAPER}.build.${VERSION_BUILD}")
    compileOnly("com.zaxxer:HikariCP:${VERSION_HIKARI}")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}