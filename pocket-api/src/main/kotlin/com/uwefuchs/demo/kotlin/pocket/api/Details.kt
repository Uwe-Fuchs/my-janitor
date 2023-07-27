package com.uwefuchs.demo.kotlin.pocket.api

/**
 * Determines the granularity of information per [Item] that will be transferred
 */
enum class Details(internal val value: String) {
    SIMPLE("simple"), COMPLETE("complete")
}