package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.application.service.OrderService;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderItem;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RestAssured.config = RestAssured.config();
    }

    @Test
    void testCreateOrder() {
        Order order = new Order(1L, "Test Order", LocalDateTime.now());
        when(orderService.createOrder(order)).thenReturn(order);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(order)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("id", is(order.getId().intValue()));
    }

    @Test
    void testGetOrder() {
        testCreateOrder();
        Order order = new Order(1L, "Test Order", LocalDateTime.now());
        when(orderService.getOrder(1L)).thenReturn(Optional.of(order));

        given()
                .pathParam("id", 1)
                .when()
                .get("/orders/{id}")
                .then()
                .statusCode(200)
                .body("description", is("Test Order"));
    }

    @Test
    void testGetAllOrders() {
        testCreateOrder();
        LocalDateTime date = LocalDateTime.now();
        Order order = new Order(1L, "Test Order", date);
        when(orderService.getOrder(1L)).thenReturn(Optional.of(order));

        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteOrder() {
        given()
                .pathParam("id", 1L)
                .when()
                .delete("/orders/{id}")
                .then()
                .statusCode(204);
    }
}
