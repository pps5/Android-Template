plugins {
    alias(libs.plugins.{{project_name_lowercase}}.android.library)
    alias(libs.plugins.{{project_name_lowercase}}.android.compose)
}

android {
    namespace = "{{application_id}}.core.ui"
}
