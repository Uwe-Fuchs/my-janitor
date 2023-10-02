package com.uwefuchs.demo.kotlin.myjanitor.web

import com.uwefuchs.demo.kotlin.pocket.ModifyTemplate
import com.uwefuchs.demo.kotlin.pocket.PocketTemplate
import com.uwefuchs.demo.kotlin.pocket.RetrieveTemplate
import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import com.uwefuchs.demo.kotlin.pocket.api.Sort
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.ui.ExtendedModelMap

class ItemControllerTest {
    private lateinit var pocket: Pocket;
    private lateinit var controllerUnderTest: ItemController;
    private lateinit var model: ExtendedModelMap;

    @BeforeEach
    fun setUp() {
        model = ExtendedModelMap();
        pocket = PocketTemplate(mock<ModifyTemplate>(), mock<RetrieveTemplate>());
        controllerUnderTest = ItemController(pocket);
    }

    @Test
    fun `given a valid request, item is retrieved`() {
        // given
        val itemsCount = 10;
        val items = itemsCount.buildItemList();
        whenever(pocket.retrieveOperations().items(sort = Sort.NEWEST)).thenReturn(items);

        // when
        val result = controllerUnderTest.overview(model);

        // then
        assertThat(result).isEqualTo("items/overview");
        assertThat(model).containsEntry("items", items);
        assertThat(model).containsEntry("count", itemsCount);
    }

    @Test
    fun `successfully delete a single item`() {
        // given
        val givenId = "id_1";
        whenever(pocket.modifyOperations().delete(givenId)).thenReturn(true);

        // when
        val result = controllerUnderTest.delete(givenId, model);

        // then
        assertThat(result).isEqualTo("redirect:/items");
        assertThat(model).containsEntry("success", true);
        assertThat(model).containsEntry("action", "deleted");
    }

    @Test
    fun `deletion failure`() {
        // given
        whenever(pocket.modifyOperations().delete(anyString())).thenReturn(false);

        // when
        val thrown: Throwable = Assertions.catchThrowable {
            controllerUnderTest.delete("someId", model);
        }

        // then
        assertThat(thrown::class).isEqualTo(PocketException::class);
        assertThat(thrown.message).contains("Item could not be deleted!");
    }

    @Test
    fun `successfully archive a single item`() {
        // given
        val givenId = "id_2";
        whenever(pocket.modifyOperations().archive(givenId)).thenReturn(true);

        // when
        val result = controllerUnderTest.archive(givenId, model);

        // then
        assertThat(result).isEqualTo("redirect:/items");
        assertThat(model).containsEntry("success", true);
        assertThat(model).containsEntry("action", "archived");
    }

    @Test
    fun `archive failure`() {
        // given
        whenever(pocket.modifyOperations().archive(anyString())).thenReturn(false);

        // when
        val thrown: Throwable = Assertions.catchThrowable {
            controllerUnderTest.archive("someId", model);
        }

        // then
        assertThat(thrown::class).isEqualTo(PocketException::class);
        assertThat(thrown.message).contains("Item could not be archived!");
    }

    @Test
    fun error() {
        // given
        val pocketException = PocketException("Oops!");

        // when
        val result = controllerUnderTest.error(model, pocketException);

        // then
        assertThat(result).isEqualTo("error");
        assertThat(model).containsEntry("cause", pocketException);
    }

    private fun Int.buildItemList(): List<Item> {
        val myList = mutableListOf<Item>();

        for (i in 0 until this) {
            myList.add(i, Item(i.toLong(), i.toLong(), "given_url_$i", "resolved_title_$i"))
        }

        return myList;
    }
}