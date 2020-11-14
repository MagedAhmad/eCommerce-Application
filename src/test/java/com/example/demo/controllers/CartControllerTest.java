package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CartControllerTest {
    private CartController cartController;

	private UserRepository userRepository = mock(UserRepository.class);

	private CartRepository cartRepository = mock(CartRepository.class);

	private ItemRepository itemRepository = mock(ItemRepository.class);

	@Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        when(userRepository.findByUsername("maged")).thenReturn(getUser());

        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(getItem()));
	}

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(2);
        request.setUsername("maged");
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(20.00), response.getBody().getTotal());
    }

    @Test
    public void add_to_cart_for_wrong_user() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(2);
        request.setUsername("wronguser");
        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("maged");
        ResponseEntity<Cart> response = cartController.addTocart(request);
        request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("maged");
        response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.valueOf(0.0), response.getBody().getTotal());
    }

    @Test
    public void remove_from_cart_for_wrong_user() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("maged");
        ResponseEntity<Cart> response = cartController.addTocart(request);
        request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("wronguser");
        response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUser() {
	    User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("maged");
        user.setPassword("testPassword");
        user.setCart(cart);

        return user;
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
