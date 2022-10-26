plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("7.4.0-beta02").apply(false)
    kotlin("multiplatform").version("1.7.10").apply(false)
    kotlin("plugin.serialization") version "1.7.10"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
