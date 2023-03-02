package com.uwefuchs.demo.kotlin.pocket.api

/**
 * the granularity of data when retrieving pocket-items
 */
enum class Details(private val value: String) {
    SIMPLE("simple"), COMPLETE("complete")
}