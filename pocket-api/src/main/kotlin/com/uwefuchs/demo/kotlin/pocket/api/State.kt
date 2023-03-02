package com.uwefuchs.demo.kotlin.pocket.api

/**
 * The State a pocket-item can have
 */
enum class State(private val value: String) {
    UNREAD("unread"), ARCHIVED("archived"), ALL("all")
}