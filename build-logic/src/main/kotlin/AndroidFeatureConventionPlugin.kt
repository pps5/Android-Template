import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            apply(plugin = "{{application_id}}.android.library")
            apply(plugin = "{{application_id}}.android.compose")
            apply(plugin = "{{application_id}}.hilt")

            dependencies {
                "implementation"(projectLibs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                "implementation"(projectLibs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                "implementation"(projectLibs.findLibrary("androidx.activity.compose").get())
                "implementation"(projectLibs.findLibrary("androidx.ui.graphics").get())
                "implementation"(projectLibs.findLibrary("androidx.compose.material").get())
                "implementation"(projectLibs.findLibrary("androidx.material.icons.extended").get())
                "implementation"(projectLibs.findLibrary("androidx.navigation.compose").get())
                "implementation"(projectLibs.findLibrary("hilt.navigation.compose").get())
                "implementation"(projectLibs.findLibrary("kotlin.coroutines.core").get())
                "testImplementation"(projectLibs.findLibrary("kotlin.coroutines.test").get())
            }
        }
    }
}