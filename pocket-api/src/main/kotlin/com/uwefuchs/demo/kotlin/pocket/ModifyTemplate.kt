package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.ModifyOperations

class ModifyTemplate(private val transport: Transport) : ModifyOperations {
    private var endpoint = "https://getpocket.com/v3/send";

    constructor(transport: Transport, endpoint: String) : this(transport) {
        this.endpoint = endpoint
    }

    override fun archive(itemIds: Collection<String>): Boolean {
        return execute(itemIds.map { Action.Archive(it) });
    }

    override fun delete(itemIds: Collection<String>): Boolean {
        return execute(itemIds.map { Action.Delete(it) });
    }

    private fun execute(actions: Collection<Action>): Boolean {
        val response = transport.modify(ModifyRequest(actions), endpoint);
        return response.status == 1;
    }
}