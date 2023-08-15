package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.Details
import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.Sort
import com.uwefuchs.demo.kotlin.pocket.api.State
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class RetrieveTemplateTest {
    private val endpoint = "http://test.de";

    @Test
    fun `retrieve items success`() {
        // given
        val itemList = buildItemList(5);
        val transport: Transport = mock<Transport> {
            on { post(any(), eq(endpoint)) } doReturn RetrieveResponse(itemList.associateBy { it.id })
        }
        val retrieveOperations = RetrieveTemplate(transport, endpoint);

        // when
        val result: Collection<Item> = retrieveOperations.items();

        // then
        assertThat(result).containsAll(itemList);
    }

    @Test
    fun `retrieve items filter unread`() {
        // given
        val itemListAll = buildItemList(5);
        val retrieveRequestAll = RetrieveRequest(State.ALL.value, Sort.OLDEST.value, Details.SIMPLE.value);
        val itemListUnread = itemListAll.subList(1, 3);
        val retrieveRequestUnread = RetrieveRequest(State.UNREAD.value, Sort.OLDEST.value, Details.SIMPLE.value);
        val transport: Transport = mock<Transport>();
        whenever(transport.post(eq(retrieveRequestAll), eq(endpoint))).thenReturn(RetrieveResponse(itemListAll.associateBy { it.id }));
        whenever(transport.post(eq(retrieveRequestUnread), eq(endpoint))).thenReturn(RetrieveResponse(itemListUnread.associateBy { it.id }));
        val retrieveOperations = RetrieveTemplate(transport, endpoint);

        // when
        val resultAll: Collection<Item> = retrieveOperations.items(state = State.ALL);

        // then
        assertThat(resultAll).containsAll(itemListAll);

        // when
        val resultUnread: Collection<Item> = retrieveOperations.items(state = State.UNREAD);

        // then
        assertThat(resultUnread).containsAll(itemListUnread);
        assertThat(resultUnread).doesNotContain(itemListAll[0], itemListAll[4]);
    }

    @Test
    fun `retrieve items emptyResult`() {
        // given
        val transport: Transport = mock<Transport> {
            on { post(any(), any()) } doReturn RetrieveResponse(emptyMap())
        }

        val retrieveOperations = RetrieveTemplate(transport, "http://test.de");

        // when
        val result: Collection<Item> = retrieveOperations.items();

        // then
        assertThat(result).isEmpty();
    }

    private fun buildItemList(listCount: Int): List<Item> {
        val myList = mutableListOf<Item>();

        for (i in 0 until listCount) {
            myList.add(i, Item(i.toLong(), i.toLong(), "given_url_$i", "resolved_title_$i"))
        }

        return myList;
    }
}