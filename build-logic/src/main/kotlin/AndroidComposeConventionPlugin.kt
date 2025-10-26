import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.findByType

internal class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val commonExtension: CommonExtension<*, *, *, *, *, *> =
                checkNotNull(
                    extensions.findByType<LibraryExtension>()
                        ?: extensions.findByType<ApplicationExtension>()
                ) { "No common extension found for ${target.name}" }

            configureAndroidCompose(commonExtension)
        }
    }
}