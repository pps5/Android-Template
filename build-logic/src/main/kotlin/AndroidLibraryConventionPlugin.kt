import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                compileSdk = projectLibs.findVersion("compileSdk").get().toString().toInt()
                defaultConfig.minSdk = projectLibs.findVersion("minSdk").get().toString().toInt()
                defaultConfig.targetSdk =
                    projectLibs.findVersion("targetSdk").get().toString().toInt()
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                compileOptions.sourceCompatibility = JavaVersion.VERSION_11
                compileOptions.targetCompatibility = JavaVersion.VERSION_11

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                testOptions.animationsDisabled = true
            }
            extensions.configure<KotlinBaseExtension> {
                val compilerOptions = when(this) {
                    is KotlinAndroidExtension -> compilerOptions
                    is KotlinJvmProjectExtension -> compilerOptions
                    else -> error("Unsupported Kotlin extension type")
                }
                compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
            }

            dependencies {
                "testImplementation"(projectLibs.findLibrary("junit").get())
                "androidTestImplementation"(projectLibs.findLibrary("androidx.junit").get())
                "androidTestImplementation"(projectLibs.findLibrary("androidx.espresso.core").get())
            }
        }
    }

}