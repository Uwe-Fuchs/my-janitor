package com.uwefuchs.demo.kotlin.pocket.api

/**
 * all operations around retrieving pocket-data
 */
interface RetrieveOperations {
    /**
     * get all pocket-items
     */
    fun items(state: State = State.UNREAD, sort: Sort = Sort.OLDEST, details: Details = Details.SIMPLE) = emptyList<Item>();
}