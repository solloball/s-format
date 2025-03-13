package ru.nsu.jexpression_path.enums

enum class PathMode(val jExpressionPrefix: String) {
    DEEP_MODE("$.."),
    DEFAULT_MODE("$");

    companion object {
        fun fromPath(path: String): PathMode {
            return PathMode.entries.find { path.startsWith(it.jExpressionPrefix) }
                ?: throw IllegalArgumentException("Failed to find mode because of illegal prefix ${path}")
        }
    }
}