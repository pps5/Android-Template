import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = projectLibs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "implementation"(projectLibs.findLibrary("androidx-compose-material").get())
            "implementation"(projectLibs.findLibrary("androidx-foundation").get())
            "implementation"(projectLibs.findLibrary("androidx-ui").get())
            "androidTestImplementation"(platform(bom))
            "implementation"(projectLibs.findLibrary("androidx-ui-tooling-preview").get())
            "debugImplementation"(projectLibs.findLibrary("androidx-ui-tooling").get())
        }
    }
}