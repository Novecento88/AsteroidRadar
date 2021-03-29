package com.udacity.asteroidradar.api


enum class AsteroidFilter(val value: String) {
    SHOW_TODAY("today"),
    SHOW_WEEK("week"),
    SHOW_ALL("all")
}