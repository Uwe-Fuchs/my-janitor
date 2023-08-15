package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class Transport(val client: OkHttpClient, val mapper: ObjectMapper, val consumerKey: String, val accessToken: String) {
    val mediaType: MediaType = "application/json; charset=utf-8".toMediaType();

    fun  post(payload: PocketRequest, endpoint: String): RetrieveResponse {
        payload.accessToken = accessToken;
        payload.consumerKey = consumerKey;

        val content = mapper.writeValueAsString(payload);
        val body = content.toRequestBody(mediaType);
        val request = Request.Builder()
            .url(endpoint)
            .post(body)
            .build();

        val response = client
            .newCall(request)
            .execute();

        response.validate();

        return mapper.readValue(response.body?.byteStream(), object: TypeReference<RetrieveResponse>() {});
    }

    fun get(endpoint: String): RetrieveResponse {
        val request = Request.Builder()
            .url("$endpoint?consumer_key=$consumerKey&access_token=$accessToken")
            .get()
            .build();

        val response = client
            .newCall(request)
            .execute();

        response.validate();

        return mapper.readValue(response.body?.byteStream(), object: TypeReference<RetrieveResponse>() {});
    }
}