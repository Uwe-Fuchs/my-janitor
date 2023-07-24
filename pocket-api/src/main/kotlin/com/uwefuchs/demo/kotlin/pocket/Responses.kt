package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.uwefuchs.demo.kotlin.pocket.api.Item

internal class RetrieveResponse(@JsonProperty("list") val items: Map<Long, Item>) {
}

internal class ModifyResponse(@JsonProperty("status") val status: Int) {
}