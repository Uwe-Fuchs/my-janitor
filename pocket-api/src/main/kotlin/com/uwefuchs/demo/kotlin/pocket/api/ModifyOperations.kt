package com.uwefuchs.demo.kotlin.pocket.api

/**
 * Operations used to modify [Item]s in Pocket
 */
interface ModifyOperations {
    fun archive(itemIds: Collection<String>): Boolean;

    fun archive(itemId: String): Boolean = archive(listOf(itemId));

    fun delete(itemIds: Collection<String>): Boolean;

    fun delete(itemId: String): Boolean = delete(listOf(itemId));
}