package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.ModifyOperations
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.RetrieveOperations

internal class PocketTemplate(consumerKey: String, accessToken: String) : Pocket {
    private val transport = Components.transport(consumerKey, accessToken);
    private val modifyOperations = ModifyTemplate(transport);
    private val retrieveOperations = RetrieveTemplate(transport);

    override fun modifyOperations(): ModifyOperations {
        return modifyOperations;
    }

    override fun retrieveOperations(): RetrieveOperations {
        return retrieveOperations;
    }
}