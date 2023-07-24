package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.ModifyOperations

internal class ModifyTemplate(private val transport: Transport) : ModifyOperations {
    private var endpoint = "https://getpocket.com/v3/send";

    constructor(transport: Transport, endpoint: String) : this(transport) {
        this.endpoint = endpoint
    }

    override fun archive(items: Collection<Item>) {
        execute(items.map { it -> Action.Archive(it) });
    }

    override fun delete(items: Collection<Item>) {
        execute(items.map { it -> Action.Delete(it) });
    }

    private fun execute(actions: Collection<Action>) {
        transport.post<ModifyResponse>(ModifyRequest(actions), endpoint);
    }
}