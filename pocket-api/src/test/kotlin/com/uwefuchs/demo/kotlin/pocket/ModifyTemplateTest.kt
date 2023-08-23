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
import kotlin.reflect.KClass

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
        val ids = arrayOf("id_1", "id_2");
        val archiveRequest = createModifyRequest(Action.Archive::class, *ids);
        whenever(transport.modify(eq(archiveRequest), eq(endpoint))).thenReturn(ModifyResponse(1));

        // when
        val classUnderTest = ModifyTemplate(transport, endpoint);
        val result = classUnderTest.archive(ids.asList());

        // then
        assertThat(result).isTrue();
        verify(transport, times(1)).modify(modifyRequestCaptor.capture(), eq(endpoint));
        assertThat(modifyRequestCaptor.firstValue).isEqualTo(archiveRequest);
    }

    @Test
    fun `archive a single item`() {
        // given
        val givenId = "id_1";
        val archiveRequest = createModifyRequest(Action.Archive::class, givenId);
        val endpointCaptor = argumentCaptor<String>();
        whenever(transport.modify(eq(archiveRequest), eq(endpoint))).thenReturn(ModifyResponse(1));

        // when
        val classUnderTest = ModifyTemplate(transport, endpoint);
        val result = classUnderTest.archive(givenId);

        // then
        assertThat(result).isTrue();
        verify(transport, times(1)).modify(modifyRequestCaptor.capture(), endpointCaptor.capture());
        assertThat(modifyRequestCaptor.firstValue).isEqualTo(archiveRequest);
        assertThat(endpointCaptor.firstValue).isEqualTo(endpoint);
    }

    @Test
    fun `delete a list of items`() {
        // given
        val ids = arrayOf("id_1", "id_2");
        val deleteRequest = createModifyRequest(Action.Delete::class, *ids);
        whenever(transport.modify(eq(deleteRequest), eq(endpoint))).thenReturn(ModifyResponse(1));

        // when
        val classUnderTest = ModifyTemplate(transport, endpoint);
        val result = classUnderTest.delete(ids.asList());

        // then
        assertThat(result).isTrue();
        verify(transport, times(1)).modify(modifyRequestCaptor.capture(), eq(endpoint));
        assertThat(modifyRequestCaptor.firstValue).isEqualTo(deleteRequest);
    }

    @Test
    fun `delete a single item`() {
        // given
        val givenId = "id_1";
        val archiveRequest = createModifyRequest(Action.Delete::class, givenId);
        val endpointCaptor = argumentCaptor<String>();
        whenever(transport.modify(eq(archiveRequest), eq(endpoint))).thenReturn(ModifyResponse(1));

        // when
        val classUnderTest = ModifyTemplate(transport, endpoint);
        val result = classUnderTest.delete(givenId);

        // then
        assertThat(result).isTrue();
        verify(transport, times(1)).modify(modifyRequestCaptor.capture(), endpointCaptor.capture());
        assertThat(modifyRequestCaptor.firstValue).isEqualTo(archiveRequest);
        assertThat(endpointCaptor.firstValue).isEqualTo(endpoint);
    }

    private fun <A: Action> createModifyRequest(modifyType: KClass<A>, vararg ids : String): ModifyRequest {
        return when (modifyType) {
            Action.Archive::class -> ModifyRequest(ids.map { id -> Action.Archive(id) });
            Action.Delete::class -> ModifyRequest(ids.map { id -> Action.Delete(id) });
            else -> throw RuntimeException("wrong Action-Type!")
        };
    }
}