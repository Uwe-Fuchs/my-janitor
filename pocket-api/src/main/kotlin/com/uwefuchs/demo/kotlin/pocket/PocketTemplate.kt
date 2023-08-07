package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.ModifyOperations
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.RetrieveOperations

class PocketTemplate(private val modifyOperations: ModifyOperations,
                     private val retrieveOperations: RetrieveOperations) : Pocket {
    override fun modifyOperations(): ModifyOperations {
        return modifyOperations;
    }

    override fun retrieveOperations(): RetrieveOperations {
        return retrieveOperations;
    }
}