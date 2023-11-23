package com.uwefuchs.demo.kotlin.pocket.testing;

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

object IntegrationTestHelper {
    fun createEntry(id: Long, title: String?, added: Long, url: String): String {
        return "\"$id\": {\"item_id\": $id, \"resolved_title\": \"$title\", \"time_added\": $added, \"given_url\": \"$url\"}";
    }

    fun createErrorResponse(responseCode: Int, errorMessage: String, server: MockWebServer) {
        val response = MockResponse();
        response.setResponseCode(responseCode);
        response.setHeader("X-Error", errorMessage);
        server.enqueue(response);
    }

    fun moveEntriesToMockServer(entries: List<String>, server: MockWebServer) {
        var myEntries = "";

        for (i in entries.indices) {
            val anEntry = entries[i];
            myEntries += anEntry;
            if (i < entries.size - 1) myEntries += ", ";
        }

        moveEntriesToMockServer(myEntries, server);
    }

    fun moveEntriesToMockServer(entries: String, server: MockWebServer) {
        val response = MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
            """
                {
                    "status" : 1,
                    "complete": 1,
                    "list" : {
                        $entries
                    }
                }
            """
        );

        server.enqueue(response);
    }
}