plugins {
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadowJar)
    java
}

var displayName = "template-server"

group = "com.github.selfcrafted"
version = "0.1.0"

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.bundles.world)
    implementation(libs.bundles.logging)
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("Name", displayName)
                property("version", version.toString())
                property("minestomVersion", libs.versions.minestom.get())
            }
        }
    }
}

tasks {
    shadowJar {
        manifest {
            attributes("Main-Class" to "com.github.selfcrafted.template.Server")
        }
        archiveBaseName.set(displayName)
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())
        mergeServiceFiles()
    }

    test {
        useJUnitPlatform()
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}