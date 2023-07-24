package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.uwefuchs.demo.kotlin.pocket.api.Item

internal sealed class Action(name: String, item: Item) {
    @JsonProperty("action") var name: String = name;
    @JsonProperty("item_id") var id: Long = item.id;

    class Archive(item: Item) : Action("archive", item);

    class Delete(item: Item)  : Action("delete", item);
}