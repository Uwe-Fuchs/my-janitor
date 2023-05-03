package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.uwefuchs.demo.kotlin.pocket.api.Item

internal class RetrieveResponse(@JsonProperty("list") val items: Map<Int, Item>) {
}