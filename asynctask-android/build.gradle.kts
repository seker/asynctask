plugins {
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

group = "io.github.seker"
version = "1.0.2-SNAPSHOT"

android {
    namespace = "seker.asynctask.android"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets {
        getByName("main") {
            java.directories.addAll(listOf("src/main/java", "../asynctask/src/main/java"))
        }
        getByName("test") {
            java.directories.addAll(listOf("src/test/java", "../asynctask/src/test/java"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release") {
            // 让官方托管源码包生成，完美包含 Java + Kotlin + 自动生成的代码
            withSourcesJar()
        }
    }
}

dependencies {
//    implementation(project(":asynctask"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.testExt.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    testImplementation(libs.junit.jupiter.api)
//    testRuntimeOnly(libs.junit.jupiter.engine)
}

publishing {
    // publish 到 output
    publications.create<MavenPublication>("Asynctask") {
        afterEvaluate {
            from(components["release"])
        }

        // 1. 三要素直接配置在最外层（等号赋值）
        groupId = project.group.toString()
        artifactId = "asynctask-android"
        version = project.version.toString()

        // 2. 规范的 POM 元数据配置（内部全部改用 .set()）
        pom {
            name.set("asynctask-android")
            description.set("async task executor for android")
            url.set("https://github.com/seker/asynctask")

            // 提示：历史代码中的 withXml 逻辑已由上面的 from(components["release"]) 完美托管，可以直接安全地删除！

            licenses {
                license {
                    name.set("GNU LESSER GENERAL PUBLIC LICENSE Version 2.1")
                    url.set("https://github.com/seker/asynctask/blob/main/LICENSE")
                }
            }

            developers {
                developer {
                    id.set("xinjian")
                    name.set("xinjian.liu")
                    email.set("04070628@163.com")
                }
            }

            scm {
                connection.set("scm:git:git@github.com:seker/asynctask.git")
                developerConnection.set("scm:git:ssh://github.com:seker/asynctask.git")
                url.set("https://github.com/seker/asynctask")
            }
        }
    }
}