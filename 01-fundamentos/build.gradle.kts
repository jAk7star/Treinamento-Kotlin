plugins {
    kotlin("jvm")
    application
}

dependencies {
    // Kotlin Standard Library
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // Testes: JUnit 5, MockK e AssertJ
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("br.com.treinamento.fundamentos.MainKt")
}
