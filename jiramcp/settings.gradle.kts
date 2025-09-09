pluginManagement {
    repositories {
        maven {
            url = uri("https://tink.jfrog.io/artifactory/gradle-plugins")
            credentials {
                username = System.getenv("JFROG_READ_USERNAME") ?: (extra["JFROG_READ_USERNAME"] as? String)
                password = System.getenv("JFROG_READ_TOKEN") ?: (extra["JFROG_READ_TOKEN"] as? String)
            }
        }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        mavenCentral()
    }
}

rootProject.name = "jiramcp"
