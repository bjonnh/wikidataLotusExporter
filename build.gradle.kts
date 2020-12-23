plugins {
    application
    kotlin("jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jmailen.kotlinter")
}

group = "net.nprod"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
}

application {
    mainClass.set("net.nprod.wikidataLotusExporter.MainKt")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "net.nprod.wikidataLotusExporter.MainKt"
    }
}

dependencies {
    val junitVersion: String by project
    val rdf4jVersion: String by project
    val log4jVersion: String by project
    val kotlinxCliVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-cli:$kotlinxCliVersion")

    implementation("org.eclipse.rdf4j:rdf4j-repository-sail:$rdf4jVersion")
    implementation("org.eclipse.rdf4j:rdf4j-sail-nativerdf:$rdf4jVersion")
    implementation("org.eclipse.rdf4j:rdf4j-core:$rdf4jVersion")
    implementation("org.eclipse.rdf4j:rdf4j-client:$rdf4jVersion")

    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

kotlinter {
    ignoreFailures = project.hasProperty("lintContinueOnError")
    experimentalRules = project.hasProperty("lintKotlinExperimental")
}

detekt {
    val detektVersion: String by project
    toolVersion = detektVersion
    config = rootProject.files("qc/detekt.yml")
    buildUponDefaultConfig = true
    baseline = rootProject.file("qc/detekt-baseline.xml")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        warn.events("PASSED", "FAILED", "SKIPPED", "STANDARD_ERROR")
        info.events("PASSED", "FAILED", "SKIPPED", "STANDARD_ERROR", "STANDARD_OUT")
        debug.events("PASSED", "FAILED", "SKIPPED", "STANDARD_ERROR", "STANDARD_OUT", "STARTED")
    }
}
