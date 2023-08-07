package com.uwefuchs.demo.kotlin.pocket.api

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
}