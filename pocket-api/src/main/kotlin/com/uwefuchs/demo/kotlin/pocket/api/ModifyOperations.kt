package com.uwefuchs.demo.kotlin.pocket.api

/**
 * Operations used to modify [Item]s in Pocket
 */
interface ModifyOperations {
    fun archive(itemIds: Collection<String>);

    fun archive(itemId: String) = archive(listOf(itemId));

    fun delete(itemIds: Collection<String>);

    fun delete(itemId: String) = delete(listOf(itemId));
}