package com.uwefuchs.demo.kotlin.myjanitor

import com.uwefuchs.demo.kotlin.pocket.api.Pocket;

fun main() {
    val name = "Janitor";
    val version = "0.0.1";
    println("$name v$version");

    val items = Pocket.connect("consumer", "access").retrieveOperations().items();
    items.forEach(::println);
}