package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.uwefuchs.demo.kotlin.pocket.api.Item

internal sealed class Action(@JsonProperty val action: String, @JsonProperty("item_id") val itemId: String) {
    class Archive(@JsonProperty("item_id") itemId: String) : Action("archive", itemId);

    class Delete(@JsonProperty("item_id") itemId: String)  : Action("delete", itemId);
}