package com.uwefuchs.demo.kotlin.pocket.api

/**
 * The sorting of a collection of pocket-items
 */
enum class Sort(internal val value: String) {
    NEWEST("newest"),
    OLDEST("oldest"),
    TITLE("title"),
    SITE("site")
}