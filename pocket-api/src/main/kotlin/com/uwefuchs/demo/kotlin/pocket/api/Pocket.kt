package com.uwefuchs.demo.kotlin.pocket.api

import com.uwefuchs.demo.kotlin.pocket.PocketTemplate

/**
 * container for all operations against our pocket-api
 */
interface Pocket {
    /**
     * delivers a reference to the ModifyOperations
     */
    fun modifyOperations(): ModifyOperations;

    /**
     * delivers a reference to the RetrieveOperations
     */
    fun retrieveOperations(): RetrieveOperations;

    companion object {
        fun connect(consumerKey: String, accessToken: String): Pocket = PocketTemplate(consumerKey, accessToken);
    }
}