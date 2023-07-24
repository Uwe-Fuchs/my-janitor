package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.Details
import com.uwefuchs.demo.kotlin.pocket.api.RetrieveOperations
import com.uwefuchs.demo.kotlin.pocket.api.Sort
import com.uwefuchs.demo.kotlin.pocket.api.State
import com.uwefuchs.demo.kotlin.pocket.api.Item


internal class RetrieveTemplate(private val transport: Transport) : RetrieveOperations {
    private var endpoint = "https://getpocket.com/v3/get";

    constructor(transport: Transport, endpoint: String) : this(transport) {
        this.endpoint = endpoint
    }

    override fun items(state: State, sort: Sort, details: Details): Collection<Item> {
        val retrieve = Retrieve(state.value, sort.value, details.value);
        val result: RetrieveResponse = transport.post(retrieve, endpoint);
        //val result: RetrieveResponse = transport.get(endpoint);
        return result
            .items
            .values;
    }
}