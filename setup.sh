#!/bin/bash

# Function to convert string to snake_case
to_snake_case() {
    local input="$1"
    # First, handle CamelCase by inserting underscores before uppercase letters
    # Then convert spaces, hyphens, and other separators to underscores
    # Finally, convert to lowercase and clean up multiple underscores
    echo "$input" | \
        sed 's/\([a-z0-9]\)\([A-Z]\)/\1_\2/g' | \
        sed 's/[[:space:]\-\.]\+/_/g' | \
        sed 's/[^a-zA-Z0-9_]/_/g' | \
        sed 's/__*/_/g' | \
        sed 's/^_\|_$//g' | \
        tr '[:upper:]' '[:lower:]'
}

# Function to read values from stdin
read_from_stdin() {
    echo "=== Android Project Setup ==="
    echo "Please provide the following information:"
    echo

    # Read project name
    echo -n "Enter project name: "
    read -r PROJECT_NAME

    # Read application ID
    echo -n "Enter application ID (e.g., com.example.myapp): "
    read -r APPLICATION_ID

    # Validate inputs
    if [[ -z "$PROJECT_NAME" ]]; then
        echo "Error: Project name cannot be empty"
        exit 1
    fi

    if [[ -z "$APPLICATION_ID" ]]; then
        echo "Error: Application ID cannot be empty"
        exit 1
    fi

    # Validate application ID format
    if [[ ! "$APPLICATION_ID" =~ ^[a-zA-Z][a-zA-Z0-9_]*(\.[a-zA-Z][a-zA-Z0-9_]*)+$ ]]; then
        echo "Warning: Application ID should follow the format 'com.example.app'"
    fi

    echo
    echo "=== Configuration Summary ==="
    echo "Project Name: $PROJECT_NAME"
    echo "Application ID: $APPLICATION_ID"

    # Generate snake_case version of project name
    PROJECT_NAME_SNAKECASE=$(to_snake_case "$PROJECT_NAME")
    echo "Project Name (snake_case): $PROJECT_NAME_SNAKECASE"

    # Generate lowercase version of project name
    PROJECT_NAME_LOWERCASE=$(echo "$PROJECT_NAME" | tr '[:upper:]' '[:lower:]')
    echo "Project Name (lowercase): $PROJECT_NAME_LOWERCASE"
    echo

    # Export for use in other scripts
    export PROJECT_NAME
    export PROJECT_NAME_SNAKECASE
    export PROJECT_NAME_LOWERCASE
    export APPLICATION_ID
    export NAMESPACE="$APPLICATION_ID"  # Namespace is typically same as application ID

    return 0
}

# Function to replace placeholders in project files
replace_placeholders() {
    echo "=== Replacing Placeholders ==="

    # Check if variables are set
    if [[ -z "$PROJECT_NAME" || -z "$APPLICATION_ID" || -z "$PROJECT_NAME_SNAKECASE" || -z "$PROJECT_NAME_LOWERCASE" ]]; then
        echo "Error: Required variables not set. Please run the setup first."
        return 1
    fi

    echo "Searching for files with placeholders..."

    # Find all files containing placeholders (excluding .git, node_modules, build directories, and this script)
    local files=($(grep -r -l "{{[^}]*}}" . \
        --exclude-dir=".git" \
        --exclude-dir="node_modules" \
        --exclude-dir="build" \
        --exclude-dir=".gradle" \
        --exclude="*.bak" \
        --exclude="setup.sh" \
        2>/dev/null | sort))

    if [[ ${#files[@]} -eq 0 ]]; then
        echo "No files with placeholders found."
        return 0
    fi

    echo "Found ${#files[@]} files with placeholders:"
    printf '  %s\n' "${files[@]}"
    echo

    echo "Replacing placeholders with:"
    echo "  {{project_name}} → $PROJECT_NAME"
    echo "  {{project_name_snakecase}} → $PROJECT_NAME_SNAKECASE"
    echo "  {{project_name_lowercase}} → $PROJECT_NAME_LOWERCASE"
    echo "  {{application_id}} → $APPLICATION_ID"
    echo

    # Replace placeholders in each file
    local processed=0

    for file in "${files[@]}"; do
        if [[ -f "$file" ]]; then
            echo "Processing: $file"

            # Replace placeholders using sed
            sed -i.tmp \
                -e "s/{{project_name}}/$PROJECT_NAME/g" \
                -e "s/{{project_name_snakecase}}/$PROJECT_NAME_SNAKECASE/g" \
                -e "s/{{project_name_lowercase}}/$PROJECT_NAME_LOWERCASE/g" \
                -e "s/{{application_id}}/$APPLICATION_ID/g" \
                "$file"

            # Remove temporary file
            rm -f "$file.tmp"

            echo "  ✓ Replaced placeholders in $file"
            ((processed++))
        else
            echo "  ⚠ File not found: $file"
        fi
    done

    echo
    echo "=== Placeholder Replacement Complete ==="
    echo "  Processed: $processed files"
    echo
    echo "Files modified:"
    printf '  %s\n' "${files[@]}"
}

# Function to rename template directories to project_name_lowercase
rename_template_directories() {
    echo "=== Renaming Template Directories ==="

    # Check if PROJECT_NAME_LOWERCASE is set
    if [[ -z "$PROJECT_NAME_LOWERCASE" ]]; then
        echo "Error: PROJECT_NAME_LOWERCASE not set. Please run the setup first."
        return 1
    fi

    echo "Searching for directories named 'template'..."

    # Find all directories named 'template' (excluding .git, build, etc.)
    local template_dirs=($(find . -type d -name "template" \
        -not -path "*/.git/*" \
        -not -path "*/build/*" \
        -not -path "*/.gradle/*" \
        -not -path "*/node_modules/*" \
        2>/dev/null | sort))

    if [[ ${#template_dirs[@]} -eq 0 ]]; then
        echo "No 'template' directories found."
        return 0
    fi

    echo "Found ${#template_dirs[@]} template directories:"
    printf '  %s\n' "${template_dirs[@]}"
    echo

    echo "Renaming template directories to: $PROJECT_NAME_LOWERCASE"

    local renamed=0

    # Process directories in reverse order (deepest first) to avoid conflicts
    for ((i=${#template_dirs[@]}-1; i>=0; i--)); do
        local old_dir="${template_dirs[i]}"
        local parent_dir=$(dirname "$old_dir")
        local new_dir="$parent_dir/$PROJECT_NAME_LOWERCASE"

        if [[ -d "$old_dir" ]]; then
            echo "Renaming: $old_dir → $new_dir"

            # Check if target directory already exists
            if [[ -d "$new_dir" ]]; then
                echo "  ⚠ Warning: Target directory already exists: $new_dir"
                echo "  Skipping rename to avoid conflicts"
            else
                # Perform the rename
                if mv "$old_dir" "$new_dir"; then
                    echo "  ✓ Successfully renamed: $old_dir → $new_dir"
                    ((renamed++))
                else
                    echo "  ✗ Failed to rename: $old_dir"
                fi
            fi
        fi
    done

    echo
    echo "=== Directory Renaming Complete ==="
    echo "  Renamed: $renamed directories"

    if [[ $renamed -gt 0 ]]; then
        echo
        echo "Note: You may need to update import statements in your code files"
        echo "to reflect the new package structure."
    fi
}

# Main script execution
read_from_stdin
replace_placeholders
rename_template_directories
