plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()

    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.architectury.dev/")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

dependencies {
    implementation(libs.indra.common)
    implementation(libs.indra.git)
    implementation(libs.shadow)
    implementation(libs.gradle.idea.ext)
    implementation(libs.architectury.plugin)
    implementation(libs.architectury.loom)

    // TODO: Add modrinth
    //implementation("com.modrinth.minotaur:Minotaur:2.7.5")
}
