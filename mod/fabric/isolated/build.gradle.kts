plugins {
    application
}

architectury {
    platformSetupLoomIde()
    fabric()
}

/**
dependencies {
    api(projects.isolation)
    compileOnlyApi(libs.fabric.api)
    compileOnlyApi(libs.fabric.loader)
}

tasks {
    jar {
        dependsOn("mod:common-base:build")
        dependsOn(":mod:fabric-base:build")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

        archiveBaseName = "floodgate-${project.name}"
        archiveVersion = ""
        archiveClassifier = ""

        //val fabricBaseJar = project.projects
        //    .fabricBase.dependencyProject
        //    .buildDir
        //    .resolve("libs")
        //    .resolve("floodgate-fabric-base.jar")

        //from(fabricBaseJar.parentFile) {
        //    include(fabricBaseJar.name)
        //    rename("floodgate-fabric-base.jar", "platform-base.jar")
        //    into("bundled/")
        //}
    }
 }
**/