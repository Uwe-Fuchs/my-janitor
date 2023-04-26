package com.uwefuchs.demo.kotlin.myjanitor

import com.uwefuchs.demo.kotlin.pocket.api.Pocket;

fun main() {
    val name = "Janitor";
    val version = "0.0.1";
    println("$name v$version");

    val pocket = Pocket.connect();
}