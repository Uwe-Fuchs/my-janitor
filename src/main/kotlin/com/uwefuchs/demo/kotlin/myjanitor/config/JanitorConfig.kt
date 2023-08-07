package com.uwefuchs.demo.kotlin.myjanitor.config

import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JanitorConfig(@Value("\${pocket.consumerKey}") private val consumerKey: String,
                    @Value("\${pocket.accessToken}") private val accessToken: String) {
    @Bean
    fun pocket(): Pocket {
        return Pocket.connect(consumerKey, accessToken);
    }
}