package common

/**
 * Enum representing climate types
 * Implements Comparable for filtering
 */
enum class Climate : Comparable<Climate> {
    RAIN_FOREST,
    MEDITERRANIAN,
    POLAR_ICECAP
}