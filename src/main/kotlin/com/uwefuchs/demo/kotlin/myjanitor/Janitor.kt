package com.uwefuchs.demo.kotlin.myjanitor;

import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Janitor(@Value("\${pocket.consumerKey}") private val consumerKey: String,
              @Value("\${pocket.accessToken}") private val accessToken: String) {
    @Bean
    open fun pocket(): Pocket {
        return Pocket.connect(consumerKey, accessToken);
    }
}

fun main(args: Array<String>) {
    runApplication<Janitor> (*args)
    val name = "Janitor";
    val version = "0.0.4";
    println("$name v$version");
}