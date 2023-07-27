package com.uwefuchs.demo.kotlin.pocket.api

/**
 * Operations used to retrieve [Item]s from Pocket
 */
interface RetrieveOperations {
    /**
     * get all pocket-items
     */
    fun items(state: State = State.UNREAD, sort: Sort = Sort.OLDEST, details: Details = Details.SIMPLE): Collection<Item>;
}