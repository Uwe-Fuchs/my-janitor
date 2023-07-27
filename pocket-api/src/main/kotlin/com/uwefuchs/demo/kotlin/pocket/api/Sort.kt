package com.uwefuchs.demo.kotlin.pocket.api

/**
 * Determines how [Item]s are sorted when retrieved from the Pocket API
 */
enum class Sort(internal val value: String) {
    NEWEST("newest"),
    OLDEST("oldest"),
    TITLE("title"),
    SITE("site")
}