package com.uwefuchs.demo.kotlin.myjanitor.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.uwefuchs.demo.kotlin.pocket.testing.IntegrationTestHelper
import com.uwefuchs.demo.kotlin.myjanitor.config.JanitorTestConfig
import com.uwefuchs.demo.kotlin.pocket.api.Item
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
import org.springframework.web.servlet.ModelAndView


@RunWith(SpringRunner::class)
@WebMvcTest
@Import(JanitorTestConfig::class)
@AutoConfigureMockMvc
class ItemControllerIT {
    @Autowired
    lateinit var mockMvc: MockMvc;

    @Autowired
    lateinit var server: MockWebServer;

    @JsonProperty("list") var items: Map<Long, Item> = HashMap();

    @Test
    fun `given a valid request, item is retrieved`() {
        // given
        val itemCount = 3;
        val createdItems: Collection<Item> = createItemListAndMoveEntriesToMockServer(itemCount);

        // when
        val result: MvcResult = mockMvc.perform(get("/items"))

        // then
        .andExpect(status().isOk())
        .andExpect(view().name("items/overview"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attribute("count", itemCount))
        .andExpect(model().attributeExists("items"))
        .andReturn();

        val modelAndView: ModelAndView = result.modelAndView as? ModelAndView ?: ModelAndView();    // Elvis-Operator :-)
        val modelMap = modelAndView.modelMap;
        val resultItems = modelMap["items"] as Collection<*>;
        assertThat(resultItems).containsAll(createdItems);
    }

    @AfterEach
    fun shutDown() {
        server.shutdown();
    }

    private fun createItemListAndMoveEntriesToMockServer(count: Int): Collection<Item> {
        val entriesList = ArrayList<String>();
        val itemsList = ArrayList<Item>();

        val addedBase: Long = 1471869712;
        for (i in 0 until count) {
            val id: Long = (229279689 + (i * 10)).toLong();
            val title = "Test_Title_$i";
            val added: Long = (addedBase + (i * 10));
            val givenUrl = "https://techdev.de";
            entriesList.add(IntegrationTestHelper.createEntry(id, title, added, givenUrl));
            itemsList.add(Item(id, added, givenUrl, title))
        }

        IntegrationTestHelper.moveEntriesToMockServer(entriesList, server);

        return itemsList;
    }
}