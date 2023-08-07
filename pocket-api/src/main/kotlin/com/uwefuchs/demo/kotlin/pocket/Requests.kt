package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty

open class PocketRequest {
    @JsonProperty("access_token") lateinit var accessToken: String;
    @JsonProperty("consumer_key") lateinit var consumerKey: String;
}

internal class RetrieveRequest(val state: String, val sort: String, @JsonProperty("detailType") val details: String) : PocketRequest() {
}

internal class ModifyRequest(val actions: Collection<Action>) : PocketRequest() {
}