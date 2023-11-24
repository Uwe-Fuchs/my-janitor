package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import com.uwefuchs.demo.kotlin.pocket.testing.IntegrationTestHelper
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class PocketIT {
    private val server = MockWebServer();

    @Test
    fun `given a valid request, item is retrieved`() {
        // given
        val id: Long = 229279689;
        val title = "A Test Title";
        val added: Long = 1471869712;
        val myEntry = IntegrationTestHelper.createEntry(id, title, added, "http://test.de");
        IntegrationTestHelper.moveEntriesToMockServer(myEntry, server);

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
        // given
        val entryWithoutTitle = "\"229279689\": {\"item_id\": 229279689, \"time_added\": 1471869712, \"given_url\": \"http://test.de\"}";
        IntegrationTestHelper.moveEntriesToMockServer(entryWithoutTitle, server);

        // when
        val items = pocket().retrieveOperations().items();

        // then
        assertThat(items).hasSize(1);
        assertThat(items.first().title).isNull();
    }

    @Test
    fun `given a bad request, PocketException is thrown`() {
        // given
        val errorMessage = "Access-Token missing or invalid";
        val responseCode = 400;
        IntegrationTestHelper.createErrorResponse(responseCode, errorMessage, server);

        // when
        val thrown: Throwable = catchThrowable {
            pocket().retrieveOperations().items();
        }

        // then
        assertThat(thrown::class).isEqualTo(PocketException::class);
        assertThat(thrown.message).contains(errorMessage, StringBuilder(responseCode));
    }

    @Test
    fun `delete a single item`() {
        // given
        // two responses: 1st for deletion, 2nd for check
        IntegrationTestHelper.moveEntriesToMockServer("", server);
        IntegrationTestHelper.moveEntriesToMockServer("", server);

        // when
        val success = pocket().modifyOperations().delete("someId");

        // then
        assertThat(success).isTrue();
        val items = pocket().retrieveOperations().items();
        assertThat(items).hasSize(0);
    }

    @Test
    fun `archive a single item`() {
        // given
        // two responses: 1st for deletion, 2nd for check
        IntegrationTestHelper.moveEntriesToMockServer("", server);
        IntegrationTestHelper.moveEntriesToMockServer("", server);

        // when
        val success = pocket().modifyOperations().archive("someId");

        // then
        assertThat(success).isTrue();
        val items = pocket().retrieveOperations().items();
        assertThat(items).hasSize(0);
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
}