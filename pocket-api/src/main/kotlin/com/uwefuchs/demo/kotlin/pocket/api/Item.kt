package com.uwefuchs.demo.kotlin.pocket.api

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Describes an item in Pocket
 * @property id the identifier as assigned by Pocket
 * @property added the time this [Item] was added (in seconds since 1970-01-01)
 * @property title the title of the corresponding article (if any)
 */
data class Item(
    @JsonProperty("item_id") val id: Long,
    @JsonProperty("time_added") val added: Long,
    @JsonProperty("resolved_title") val title: String?
) { }