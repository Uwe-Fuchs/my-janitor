package com.uwefuchs.demo.kotlin.myjanitor.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.uwefuchs.demo.kotlin.myjanitor.config.JanitorTestConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view


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
        createItemListInMockServer(3);

        // when
        val result: MvcResult = mockMvc.perform(get("/items"))

        // then
        .andExpect(status().isOk())
        .andExpect(view().name("items/overview"))
        .andExpect(model().attribute("count", 3))
        //.andExpect(MockMvcResultMatchers.model().attribute("items", 3))
        .andReturn();

        val resultResponseJson = result.response.contentAsString;
        assertThat(resultResponseJson).isNotNull();

        // TODO: check result-item
    }

    @AfterEach
    fun shutDown() {
        server.shutdown();
    }

    private fun createItemListInMockServer(count: Int) {
        val addedBase: Long = 1471869712
        var myEntries = "";
        for (i in 0 until count) {
            val id: String = (229279689 + (i * 10)).toString();
            val title = "Test_Title_$i";
            val added: String = (addedBase + (i * 10)).toString();
            myEntries += "\"$id\": {\"item_id\": $id, \"resolved_title\": \"$title\", \"time_added\": $added, \"given_url\": \"https://techdev.de\"}";
            if (i < count - 1) myEntries += ", ";
        }

        val response = MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
            """
                {
                    "status" : 1,
                    "complete": 1,
                    "list" : {
                        $myEntries
                    }
                }
            """
        );

        server.enqueue(response);

        //return objectMapper.readValue(response.getBody().toString(), List::class.java);
    }
}