package com.uwefuchs.demo.kotlin.myjanitor.web

import com.uwefuchs.demo.kotlin.myjanitor.config.JanitorTestConfig
import com.uwefuchs.demo.kotlin.pocket.ModifyTemplate
import com.uwefuchs.demo.kotlin.pocket.RetrieveTemplate
import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import com.uwefuchs.demo.kotlin.pocket.testing.IntegrationTestHelper
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
    private val server = MockWebServer();

    @Autowired
    lateinit var mockMvc: MockMvc;

    @Autowired
    lateinit var modifyTemplate: ModifyTemplate;

    @Autowired
    lateinit var retrieveTemplate: RetrieveTemplate;

    @Autowired
    lateinit var pocket: Pocket;

    @Test
    fun `given a valid request, items are retrieved`() {
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

        val modelAndView: ModelAndView = result.modelAndView ?: ModelAndView();    // Elvis-Operator :-)
        val resultItems = modelAndView.modelMap["items"] as Collection<*>;
        assertThat(resultItems).containsAll(createdItems);
    }

    @Test
    fun `delete a single item`() {
        // given
        createItemListAndMoveEntriesToMockServer(2);
        createItemListAndMoveEntriesToMockServer(1);

        // when
        mockMvc.perform(get("/items/delete")
            .queryParam("id", "someId"))

        // then
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/items"))
        .andReturn();

        mockMvc.perform(get("/items"))
        .andExpect(status().isOk())
        .andExpect(view().name("items/overview"))
        .andExpect(model().attribute("count", 1));
    }

    @Test
    fun `archive a single item`() {
        // given
        createItemListAndMoveEntriesToMockServer(2);
        createItemListAndMoveEntriesToMockServer(1);

        // when
        mockMvc.perform(get("/items/archive")
            .queryParam("id", "someId"))

        // then
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/items"))
        .andReturn();

        mockMvc.perform(get("/items"))
            .andExpect(status().isOk())
            .andExpect(view().name("items/overview"))
            .andExpect(model().attribute("count", 1));
    }

    @Test
    fun error() {
        // given
        val httpStatusCode = 503;
        val response = MockResponse();
        response.setResponseCode(httpStatusCode);
        server.enqueue(response);

        // when
        val result: MvcResult = mockMvc.perform(get("/items"))

        // then
        .andExpect(status().isOk())
        .andExpect(view().name("error"))
        .andExpect(model().attributeExists("cause"))
        .andReturn();

        val modelAndView: ModelAndView = result.modelAndView ?: ModelAndView();
        val resultError = modelAndView.modelMap["cause"] as PocketException;
        assertThat(resultError.message).isEqualTo("Error during Pocket-operation: null [status $httpStatusCode]");
    }

    @Test
    fun `deletion failure`() {
        // given
        val response = MockResponse();
        response.setResponseCode(503);
        server.enqueue(MockResponse());

        // when
        mockMvc.perform(get("/items/delete")
            .queryParam("id", "someId"))

        // then
        .andExpect(status().isOk())
        .andExpect(view().name("error"))
        .andExpect(model().attributeExists("cause"))
        .andReturn();
    }

    @Test
    fun `archive failure`() {
        // given
        val response = MockResponse();
        response.setResponseCode(503);
        server.enqueue(MockResponse());

        // when
        mockMvc.perform(get("/items/archive")
            .queryParam("id", "someId"))

        // then
        .andExpect(status().isOk())
        .andExpect(view().name("error"))
        .andExpect(model().attributeExists("cause"))
        .andReturn();
    }

    @BeforeEach
    fun setUp() {
        server.start();
        retrieveTemplate.endpoint = server.url("/v3/get").toString();
        modifyTemplate.endpoint = server.url("/v3/send").toString();
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