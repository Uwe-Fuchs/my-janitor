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

    // 1st read
    val items: Collection<Item> = pocket.retrieveOperations().items();
    items.forEach(::println);

    // archive one item:
    val item2archive = items.toList()[3];
    println("item to archive v$item2archive");
    pocket.modifyOperations().archive(item2archive);

    // 2nd read
    val itemsAfterArchiving: Collection<Item> = pocket.retrieveOperations().items();
    itemsAfterArchiving.forEach(::println);

    // delete one item:
    val item2delete = itemsAfterArchiving.first();
    println("item to delete v$item2delete");
    pocket.modifyOperations().delete(item2delete);

    // 3rd read:
    val itemsAfterDeletion: Collection<Item> = pocket.retrieveOperations().items();
    itemsAfterDeletion.forEach(::println);
}