package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import okhttp3.Response

fun Response.validate() {
    if (this.isSuccessful.not()) {
        val error = this.header("X-Error");
        throw PocketException("Error during Pocket-operation: $error [status ${this.code}]")
    }
}
