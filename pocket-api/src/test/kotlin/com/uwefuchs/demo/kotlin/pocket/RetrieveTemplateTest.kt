package com.uwefuchs.demo.kotlin.pocket

import com.uwefuchs.demo.kotlin.pocket.api.Details
import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.Sort
import com.uwefuchs.demo.kotlin.pocket.api.State
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RetrieveTemplateTest {
    private val endpoint = "http://test.de";

    @Test
    fun `retrieve items success`() {
        // given
        val listCount = 5;
        val itemList = buildItemList(listCount);
        val retrieveRequest = RetrieveRequest(State.ALL.value, Sort.OLDEST.value, Details.SIMPLE.value);
        val transport: Transport = mock<Transport>();
        whenever(transport.post(eq(retrieveRequest), eq(endpoint)))
            .thenReturn(RetrieveResponse(itemList.associateBy { it.id }));

        // when
        val classUnderTest = RetrieveTemplate(transport, endpoint);
        val result: Collection<Item> = classUnderTest.items(state = State.ALL);

        // then
        assertThat(result).hasSize(listCount);
        assertThat(result).containsAll(itemList);
    }

    @Test
    fun `retrieve items filter unread`() {
        // given
        val itemListAll = buildItemList(5);
        val itemListUnread = itemListAll.subList(1, 3);
        val retrieveRequestUnread = RetrieveRequest(State.UNREAD.value, Sort.OLDEST.value, Details.SIMPLE.value);
        val transport: Transport = mock<Transport>();
        whenever(transport.post(eq(retrieveRequestUnread), eq(endpoint)))
            .thenReturn(RetrieveResponse(itemListUnread.associateBy { it.id }));

        // when
        val classUnderTest = RetrieveTemplate(transport, endpoint);
        val result: Collection<Item> = classUnderTest.items(state = State.UNREAD);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsAll(itemListUnread);
        assertThat(result).doesNotContain(itemListAll[0], itemListAll[3], itemListAll[4]);
    }

    @Test
    fun `retrieve items emptyResult`() {
        // given
        val itemList = buildItemList(0);
        val transport: Transport = mock<Transport>();
        whenever(transport.post(any(), any())).thenReturn(RetrieveResponse(itemList.associateBy { it.id }));

        // when
        val classUnderTest = RetrieveTemplate(transport, endpoint);
        val result: Collection<Item> = classUnderTest.items();

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