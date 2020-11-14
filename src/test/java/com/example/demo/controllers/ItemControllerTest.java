package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.apache.coyote.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {
    private ItemController itemController;

	private ItemRepository itemRepository = mock(ItemRepository.class);

	@Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        when(itemRepository.findAll()).thenReturn(Collections.singletonList(getItem()));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(getItem()));
        when(itemRepository.findByName("New Item")).thenReturn(Collections.singletonList(getItem()));
	}

    @Test
    public void get_all_items() {
	    ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_id() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals("New item", response.getBody().getName());
	}

    private Item getItem() {
	    Item item = new Item();
        item.setId(1L);
        item.setName("New item");
        BigDecimal price = BigDecimal.valueOf(10.00);
        item.setPrice(price);
        item.setDescription("A new item description");

        return item;
    }
}
