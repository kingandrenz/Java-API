package com.flex.orders.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.flex.orders.service.OrderService;
import com.flex.orders.service.JwtService;
import com.flex.orders.mapper.OrderMapperImpl;
import com.flex.orders.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.orders.exception.OrderNotFoundException;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(OrderMapperImpl.class)
public class OrderControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;
        @MockitoBean
        private OrderService orderService;

        @MockitoBean
        private JwtService jwtService;

        @Test
        void getOrders_ShouldReturnListOfOrders() throws Exception {
                Order order = new Order("Anthony", 200.0, "COMPLETED");
                Order order2 = new Order("Flex", 5000.0, "PENDING");

                when(orderService.getAll()).thenReturn(List.of(order, order2));

                mockMvc.perform(get("/orders"))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.length()").value(2),
                                                jsonPath("$.[0].customerName").value("Anthony"),
                                                jsonPath("$.[1].customerName").value("Flex"),
                                                jsonPath("$.[0].amount").value(200.0),
                                                jsonPath("$.[1].amount").value(5000.0));
        }

        @Test
        void getOrder_shouldReturnOrderById() throws Exception {
                Order order = new Order("Anthony", 200.0, "COMPLETED");

                when(orderService.getById(1)).thenReturn(order);

                mockMvc.perform(get("/orders/1"))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.customerName").value("Anthony"),
                                                jsonPath("$.amount").value(200.0));
        }

        @Test
        void getOrder_shouldReturnNotFound_whenOrderDoesNotExist() throws Exception {
                when(orderService.getById(999)).thenThrow(new OrderNotFoundException(999));

                mockMvc.perform(get("/orders/999"))
                                .andExpectAll(
                                                status().isNotFound(),
                                                jsonPath("$.status").value(404),
                                                jsonPath("$.message").value("Order not found with ID: 999"));
        }

        @Test
        void createOrder_shouldReturnCreatedWhenValueIsValid() throws Exception {
                Order request = new Order("Claude", 300.0, "PENDING");
                Order response = new Order("Claude", 300.0, "PENDING");

                when(orderService.addOrder(any(Order.class))).thenReturn(response);

                mockMvc.perform(post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(
                                                status().isCreated(),
                                                jsonPath("$.customerName").value("Claude"),
                                                jsonPath("$.amount").value(300.0),
                                                jsonPath("$.status").value("PENDING"));
        }

        @Test
        void createOrder_shouldReturnBadRequest_whenCustomerNameIsEmpty() throws Exception {
                Order request = new Order("", 300.0, "PENDING");

                mockMvc.perform(post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.status").value(400),
                                                jsonPath("$.message").value("customerName: Customer name is required"));
        }

        @Test
        void updateOrder_shouldReturnUpdatedOrder() throws Exception {
                Order request = new Order("Andrenz", 300.0, "PENDING");
                Order response = new Order("King Andrenz", 350.0, "PENDING");

                when(orderService.updateOrder(eq(1), any(Order.class))).thenReturn(response);

                mockMvc.perform(put("/orders/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.customerName").value("King Andrenz"),
                                                jsonPath("$.amount").value(350.0),
                                                jsonPath("$.status").value("PENDING"));
        }

        @Test
        void shouldReturnNoContentWhenDeleteOrder() throws Exception {
                mockMvc.perform(delete("/orders/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

}
