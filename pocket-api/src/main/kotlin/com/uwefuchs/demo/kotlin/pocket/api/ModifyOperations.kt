package com.uwefuchs.demo.kotlin.pocket.api

/**
 * all operations around modifying pocket-data
 */
interface ModifyOperations {
    fun archive(items: Collection<Item>);

    fun archive(item: Item) = archive(listOf(item));

    fun delete(items: Collection<Item>);

    fun delete(item: Item) = delete(listOf(item));
}