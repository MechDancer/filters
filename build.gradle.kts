import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72" apply (true)
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

group = "org.mechdancer"
version = "0.1.0"

repositories {
    maven("https://maven.aliyun.com/repository/jcenter")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // 矩阵运算
    implementation("org.mechdancer", "linearalgebra", "0.2.8-snapshot-3")

    testImplementation("junit", "junit", "+")
    testImplementation(kotlin("test-junit"))

    // 支持网络工具
    testImplementation(kotlin("reflect"))
    testImplementation("org.mechdancer", "dependency", "+")
    testImplementation("org.mechdancer", "remote", "+")
    testImplementation("org.slf4j", "slf4j-api", "+")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions { freeCompilerArgs = listOf("-XXLanguage:+InlineClasses") }
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions { freeCompilerArgs = listOf("-XXLanguage:+InlineClasses") }
    }
}

tasks.dokka {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/javadoc"
}
val doc = tasks.register<Jar>("javadocJar") {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

val sources = tasks.register<Jar>("sourcesJar") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "Creates sources jar"
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val fat = tasks.register<Jar>("fatJar") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "Packs binary output with dependencies"
    archiveClassifier.set("all")
    from(sourceSets.main.get().output)
    from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
}

tasks.register("allJars") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "Assembles all jars in one task"
    dependsOn(doc, sources, fat, tasks.jar)
}

val rename = tasks.register("renamePomFile") {
    dependsOn(tasks.publishToMavenLocal)
    doLast {
        val path = "${buildDir.absolutePath}/publications/maven/"
        val old = File(path + "pom-default.xml")
        val f = File("$path${project.name}-$version.pom")
        old.renameTo(f)
    }
}

tasks.bintrayUpload.configure {
    dependsOn(rename)
}

bintray {
    user = "berberman"
    key = System.getenv("BintrayToken")
    setConfigurations("archives")
    val v = version.toString()
    val url = "https://github.com/MechDancer/filters"
    publish = true
    pkg.apply {
        name = project.name
        desc = "filters kotlin utilities"
        repo = "maven"
        userOrg = "mechdancer"
        githubRepo = "MechDancer/filters"
        vcsUrl = "$url.git"
        issueTrackerUrl = "$url/issues"
        publicDownloadNumbers = true
        setLicenses("WTFPL")
        version.apply {
            name = v
            vcsTag = v
            websiteUrl = "$url/releases/tag/$v"
        }
    }
}

publishing {
    repositories {
        maven("$buildDir/repo")
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MechDancer/filters")
            credentials {
                username = "MechDancerProject"
                password = System.getenv("GitHubToken")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

artifacts {
    add("archives", tasks.jar)
    add("archives", fat)
    add("archives", sources)
    add("archives", doc)
    add("archives", File("${buildDir.absolutePath}/publications/maven/${project.name}-$version.pom"))
}
