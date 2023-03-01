package com.uwefuchs.demo.kotlin

import java.io.PrintStream as Printer

fun main(): Unit {
    val name = "Janitor"
    val version = "0.0.1"

    fun Printer.echo() = "$name v$version"

    println(System.out.echo())
}