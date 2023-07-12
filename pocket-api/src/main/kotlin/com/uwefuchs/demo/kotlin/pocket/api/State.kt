package com.uwefuchs.demo.kotlin.pocket.api

/**
 * The State a pocket-item can have
 */
enum class State(internal val value: String) {
    UNREAD("unread"),
    ARCHIVED("archived"),
    ALL("all")
}