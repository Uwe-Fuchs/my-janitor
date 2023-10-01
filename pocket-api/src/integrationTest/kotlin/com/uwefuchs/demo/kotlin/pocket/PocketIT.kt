package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class PocketIT {
    var server = MockWebServer();

    @Test
    fun `given a valid request, item is retrieved`() {
        // given
        val id: Long = 229279689;
        val title = "A Test Title";
        val added: Long = 1471869712;
        server.enqueue(item(id, title, added));

        // when
        val items = pocket().retrieveOperations().items();

        // then
        assertThat(items).hasSize(1);
        assertThat(items.first().title).isEqualTo(title);
        assertThat(items.first().id).isEqualTo(id);
        assertThat(items.first().added).isEqualTo(added);
    }

    @Test
    fun `given an item without title, no exception is thrown`() {
        // given when
        server.enqueue(itemWithoutTitle("http://someurl.com"));

        // when
        val items = pocket().retrieveOperations().items();

        // then
        assertThat(items).hasSize(1);
        assertThat(items.first().title).isNull();
    }

    @Test
    fun `given a bad request, PocketException is thrown`() {
        // given
        val errorMessage = "Missing API Key";
        val responseCode = 400;
        server.enqueue(error(responseCode, errorMessage))

        // when
        val thrown: Throwable = catchThrowable {
            pocket().retrieveOperations().items();
        }

        // then
        assertThat(thrown::class).isEqualTo(PocketException::class);
        assertThat(thrown.message).contains(errorMessage, StringBuilder(responseCode));
    }

    @BeforeEach
    fun setUp() {
        server.start();
    }

    @AfterEach
    fun shutDown() {
        server.shutdown();
    }

    private fun pocket(): Pocket {
        val pocket = mock(Pocket::class.java);
        val transport = Components.transport("consumer", "access");
        `when`(pocket.modifyOperations()).thenReturn(ModifyTemplate(transport, server.url("/v3/send").toString()));
        `when`(pocket.retrieveOperations()).thenReturn(RetrieveTemplate(transport, server.url("/v3/get").toString()));

        return pocket;
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

    private fun itemWithoutTitle(url: String): MockResponse {
        val response = MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
            """
                {
                    "status" : 1,
                    "list" : {
                        "229279689" : {
                            "item_id" : "229279689",
                            "time_added" : "1471869712",
                            "given_url" : "$url"
                        }
                    }
                }
            """
        );

        return response;
    }

    private fun error(responseCode: Int, errorMessage: String): MockResponse {
        val response = MockResponse();
        response.setResponseCode(responseCode);
        response.setHeader("X-Error", errorMessage);
        return response;
    }
}