package com.uwefuchs.demo.kotlin.pocket.api

/**
 * Operations used to modify [Item]s in Pocket
 */
interface ModifyOperations {
    fun archive(items: Collection<Item>);

    fun archive(item: Item) = archive(listOf(item));

    fun delete(items: Collection<Item>);

    fun delete(item: Item) = delete(listOf(item));
}