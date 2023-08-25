package com.uwefuchs.demo.kotlin.myjanitor;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Janitor {}

fun main(args: Array<String>) {
    runApplication<Janitor> (*args)
    val name = "Janitor";
    val version = "0.0.7";
    println("$name v$version");
}