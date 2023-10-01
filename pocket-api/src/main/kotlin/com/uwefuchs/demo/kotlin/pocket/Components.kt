package com.uwefuchs.demo.kotlin.pocket

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient

object Components {
    fun client(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(PocketInterceptor).build();
    }

    fun mapper(): ObjectMapper {
        return jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    fun transport(consumerKey: String, accessToken: String): Transport {
        return Transport(client(), mapper(), consumerKey, accessToken);
    }
}