package com.uwefuchs.demo.kotlin.myjanitor.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uwefuchs.demo.kotlin.myjanitor.config.JanitorTestConfig
import com.uwefuchs.demo.kotlin.pocket.api.Item
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
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

    @JsonProperty("list") var items: Map<Long, Item> = HashMap();

    @Test
    fun `given a valid request, item is retrieved`() {
        // given
        val createdItems: Collection<Item> = createItemListInMockServer(3);

        // when
        val result: MvcResult = mockMvc.perform(get("/items"))

        // then
        .andExpect(status().isOk())
        .andExpect(view().name("items/overview"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attribute("count", 3))
        .andExpect(model().attributeExists("items"))
        //.andExpect(model().attribute("items", createdItems))
        .andReturn();

    }

    @AfterEach
    fun shutDown() {
        server.shutdown();
    }

    private fun createItemListInMockServer(count: Int): Collection<Item> {
        val addedBase: Long = 1471869712
        var myEntries = "";
        for (i in 0 until count) {
            val id = (229279689 + (i * 10)).toString();
            val title = "Test_Title_$i";
            val added = (addedBase + (i * 10)).toString();
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

        val entriesList = myEntries.split("},").toMutableList();
        for (n in entriesList.indices) {
            if (entriesList[n].endsWith("}")) {
                entriesList[n] = entriesList[n].removeSuffix("}");
            }

            entriesList[n] = entriesList[n].substringAfter("{");
        }

        val itemsMapList = ArrayList<Map<String, String>>();

        for (line in entriesList) {
            val newLine = line.replace("\"", "");
            val itemsAsMap = HashMap<String, String>();
            for (itemString in newLine.split(", ")) {
                itemsAsMap[itemString.split(": ")[0]] = itemString.split(": ")[1];
            }

            itemsMapList.add(itemsAsMap);
        }

        val itemsList = ArrayList<Item>();
        for (itemsMap in itemsMapList) {
            val itemId: Long = (itemsMap["item_id"] ?: "0").toLong();
            val timeAdded: Long = (itemsMap["time_added"] ?: "0").toLong();
            val givenUrl: String = itemsMap["given_url"] ?: "unknown"
            val resolvedTitle: String = itemsMap["resolved_title"] ?: "unknown"
            val item = Item(itemId, timeAdded, givenUrl, resolvedTitle);
            itemsList.add(item);
        }

        return itemsList;
    }
}