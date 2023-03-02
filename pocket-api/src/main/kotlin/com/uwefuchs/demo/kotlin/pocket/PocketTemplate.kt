package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.ModifyOperations
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.RetrieveOperations

internal class PocketTemplate : Pocket {
    private val modifyOperations = ModifyTemplate();
    private val retrieveOperations = RetrieveTemplate();

    override fun modifyOperations(): ModifyOperations {
        return modifyOperations;
    }

    override fun retrieveOperations(): RetrieveOperations {
        return retrieveOperations;
    }
}