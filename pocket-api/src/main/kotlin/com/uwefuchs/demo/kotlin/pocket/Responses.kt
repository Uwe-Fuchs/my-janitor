package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.uwefuchs.demo.kotlin.pocket.api.Item

class RetrieveResponse(@JsonProperty("list") val items: Map<Long, Item>) {
}

class ModifyResponse(@JsonProperty("status") val status: Int) {
}