plugins {
    id("checkstyle")
    id("java")
    id("pmd")
}

group = "org.github.javajake"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.12.2"
}

pmd {
    ruleSetFiles = files("config/pmd/pmd.xml")
}