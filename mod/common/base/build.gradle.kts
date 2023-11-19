architectury {
    common("fabric")
}

loom {
    accessWidenerPath.set(file("src/main/resources/floodgate.accesswidener"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

dependencies {
    api(projects.core)

    compileOnly(libs.mixin)
    annotationProcessor(projects.core)
    annotationProcessor(libs.micronaut.inject.java)
    compileOnlyApi(projects.isolation)
}