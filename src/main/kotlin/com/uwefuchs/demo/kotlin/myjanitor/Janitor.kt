package com.uwefuchs.demo.kotlin.myjanitor;

import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.Pocket;

fun main() {
    val name = "Janitor";
    val version = "0.0.1";
    println("$name v$version");

    val consumerKey: String = System.getenv("consumerKey");
    val accessToken = System.getenv("accessToken");

    val pocket: Pocket = Pocket.connect(consumerKey, accessToken);
    val items: Collection<Item> = pocket.retrieveOperations().items();
    items.forEach(::println);
}