# {{project_name}}

## Directory Structure

The project follows a standard Android application structure.

- app: The main module of the application.
- core:
    - di: Shared dependency injection classes such as Qualifiers
    - ui: Shared UI components, themes, colors, etc.
- feature: The root directory for feature modules. Each feature module is placed here.
- domain: App-specific logic modules. Each submodule cannot depend on any other project non-domain modules
    - models: Model classes
    - repositories: Repository interfaces
    - usecases: UseCase implementations
- infrastructure:
    - db: Database-related code packages such as DAOs and entities
    - file: File-related code packages
    - openai: ChatGPT API-related code packages
    - repositories: Repository implementation packages

## Coding Style Guidelines

### Kotlin

- Follow the official Kotlin guidelines
- Use trailing commas at the end of parameters
- Do not specify default values for lambda parameters

## Convention Plugins

- AndroidComposeConventionPlugin for modules with Jetpack Compose
- AndroidFeatureConventionPlugin for feature modules. This includes Compose and library convention plugins
- AndroidLibraryConventionPlugin for modules without Compose and features
- HiltConventionPlugin for Hilt modules. This can be used with the library convention plugin
