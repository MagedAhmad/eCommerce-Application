package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

	private UserRepository userRepository = mock(UserRepository.class);
	private OrderRepository orderRepository = mock(OrderRepository.class);

	@Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        when(userRepository.findByUsername("maged")).thenReturn(getUser());
	}

    @Test
    public void submit_new_order_happy_path() {
	    ResponseEntity<UserOrder> response = orderController.submit("maged");
	    assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void submit_new_order_for_wrong_user() {
	    ResponseEntity<UserOrder> response = orderController.submit("wronguser");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_happy_path() {
	    ResponseEntity<List<UserOrder>> orders = orderController.getOrdersForUser("maged");
        assertNotNull(orders);
        assertEquals(200, orders.getStatusCodeValue());
	}

	@Test
    public void get_orders_for_wrong_user() {
	    ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("wronguser");
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUser() {
	    User user = new User();
        user.setId(0);
        user.setUsername("maged");
        user.setPassword("testPassword");
        user.setCart(getCart(user));

        return user;
    }

    private Cart getCart(User user) {
	    Item item = new Item();
        item.setId(1L);
        item.setName("New item");
        BigDecimal price = BigDecimal.valueOf(10.00);
        item.setPrice(price);
        item.setDescription("A new item description");
        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);

        return cart;
    }

}
