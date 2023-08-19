package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty

internal sealed class Action(@field:JsonProperty("action") val actionName: String, open val itemId: String) {
    data class Archive(@field:JsonProperty("item_id") override val itemId: String) : Action("archive", itemId);

    data class Delete(@field:JsonProperty("item_id") override val itemId: String)  : Action("delete", itemId);
}