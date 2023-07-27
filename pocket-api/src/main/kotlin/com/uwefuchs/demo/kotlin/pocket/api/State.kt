package com.uwefuchs.demo.kotlin.pocket.api

/**
 * The state [Item]s can have
 */
enum class State(internal val value: String) {
    UNREAD("unread"),
    ARCHIVED("archived"),
    ALL("all")
}