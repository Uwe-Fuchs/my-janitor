package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

internal class Transport(private val client: OkHttpClient, private val mapper: ObjectMapper, private val consumerKey: String, private val accessToken: String) {
    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType();

    inline fun <reified T: Any> post(payload: PocketRequest, endpoint: String): T {
        payload.access = accessToken;
        payload.consumer = consumerKey;

        val content = mapper.writeValueAsString(payload);
        val body = content.toRequestBody(mediaType);
        val request = Request.Builder()
            .url(endpoint)
            .post(body)
            .build();

        val response = client
            .newCall(request)
            .execute();

        if (!response.isSuccessful) {
            val error = response.header("X-Error");
            throw PocketException("$error [status ${response.code}]")
        }

        return mapper.readValue(response.body?.byteStream(), object: TypeReference<T>() {});
    }

    fun get(endpoint: String): RetrieveResponse {
        val request = Request.Builder()
            .url("$endpoint?consumer_key=$consumerKey&access_token=$accessToken")
            .get()
            .build();

        val response = client
            .newCall(request)
            .execute();

        return when(response.code) {
            200 -> mapper.readValue(response.body?.byteStream(), object: TypeReference<RetrieveResponse>() {});
            else -> throw PocketException("Error when retrieving items from Pocket: [status ${response.code}]");
        }
    }
}