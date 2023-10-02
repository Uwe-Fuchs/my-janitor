package com.uwefuchs.demo.kotlin.myjanitor.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.uwefuchs.demo.kotlin.myjanitor.config.JanitorTestConfig
import com.uwefuchs.demo.kotlin.pocket.api.Item
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@RunWith(SpringRunner::class)
@WebMvcTest
@Import(JanitorTestConfig::class)
@AutoConfigureMockMvc
class ItemControllerIT {
    @Autowired
    lateinit var mockMvc: MockMvc;

    @Autowired
    lateinit var objectMapper: ObjectMapper;

    @Autowired
    lateinit var server: MockWebServer;

    @Test
    fun `given a valid request, item is retrieved`() {
        // given
        val id: Long = 229279689;
        val title = "A Test Title";
        val added: Long = 1471869712;
        server.enqueue(item(id, title, added));

        // when
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/items"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("items/overview"))
        // TODO: check result-item

        assertThat(true).isTrue();
    }

    @BeforeEach
    fun setUp() {
        //server.start();
    }

    @AfterEach
    fun shutDown() {
        server.shutdown();
    }

    private fun item(id: Long, title: String, added: Long): MockResponse {
        val response = MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
            """
                {
                    "status" : 1,
                    "list" : {
                        "$id" : {
                            "item_id" : $id,
                            "resolved_title" : "$title",
                            "time_added" : $added,
                            "given_url" : "https://techdev.de"
                        }
                    }
                }
            """
        );

        return response;
    }
}