package com.uwefuchs.demo.kotlin.myjanitor.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uwefuchs.demo.kotlin.pocket.ModifyTemplate
import com.uwefuchs.demo.kotlin.pocket.PocketInterceptor
import com.uwefuchs.demo.kotlin.pocket.RetrieveTemplate
import com.uwefuchs.demo.kotlin.pocket.PocketTemplate
import com.uwefuchs.demo.kotlin.pocket.Transport
import com.uwefuchs.demo.kotlin.pocket.api.ModifyOperations
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.RetrieveOperations
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("com.uwefuchs.demo.kotlin.pocket")
class JanitorConfig(@Value("\${pocket.consumerKey}") private val consumerKey: String,
                    @Value("\${pocket.accessToken}") private val accessToken: String) {
    private fun client(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(PocketInterceptor).build();
    }

    private fun mapper(): ObjectMapper {
        return jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    fun transport(): Transport {
        return Transport(client(), mapper(), consumerKey, accessToken);
    }

    @Bean
    fun modifyOperations(@Autowired transport: Transport): ModifyOperations {
        return ModifyTemplate(transport);
    }

    @Bean
    fun retrieveOperations(@Autowired transport: Transport): RetrieveOperations {
        return RetrieveTemplate(transport);
    }

    @Bean
    fun pocket(@Autowired modifyOperations: ModifyOperations, @Autowired retrieveOperations: RetrieveOperations): Pocket {
        return PocketTemplate(modifyOperations, retrieveOperations);
    }
}