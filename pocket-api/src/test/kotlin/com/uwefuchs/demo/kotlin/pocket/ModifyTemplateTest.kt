package com.uwefuchs.demo.kotlin.pocket

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.verify
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever

class ModifyTemplateTest {
    private val endpoint = "http://test.de";
    private lateinit var transport: Transport;
    private lateinit var modifyRequestCaptor: KArgumentCaptor<ModifyRequest>;

    @BeforeEach
    fun setUp() {
        transport = mock<Transport>();
        modifyRequestCaptor = argumentCaptor<ModifyRequest>();
    }

    @Test
    fun `archive a list of items`() {
        // given
        val idList = listOf("id_1", "id_2");
        val modifyRequest = ModifyRequest(idList.map { id -> Action.Archive(id) });
        whenever(transport.archive(eq(modifyRequest), eq(endpoint))).thenReturn(ModifyResponse(1));

        // when
        val classUnderTest = ModifyTemplate(transport, endpoint);
        val result = classUnderTest.archive(idList);

        // then
        assertThat(result).isTrue();
        verify(transport, times(1)).archive(modifyRequestCaptor.capture(), eq(endpoint));
        assertThat(modifyRequestCaptor.firstValue).isEqualTo(modifyRequest);
    }

    @Test
    fun `archive a single item`() {
        // given
        val givenId = "id_1";
        val modifyRequest = ModifyRequest(listOf(Action.Archive(givenId)));
        val endpointCaptor = argumentCaptor<String>();
        whenever(transport.archive(eq(modifyRequest), eq(endpoint))).thenReturn(ModifyResponse(1));

        // when
        val classUnderTest = ModifyTemplate(transport, endpoint);
        val result = classUnderTest.archive(givenId);

        // then
        assertThat(result).isTrue();
        verify(transport, times(1)).archive(modifyRequestCaptor.capture(), endpointCaptor.capture());
        assertThat(modifyRequestCaptor.firstValue).isEqualTo(modifyRequest);
        assertThat(endpointCaptor.firstValue).isEqualTo(endpoint);
    }

//    @Test
//    fun `delete a list of items`() {
//    }
//
//    @Test
//    fun `delete a single item`() {
//    }
}