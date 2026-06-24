package com.flex.orders.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.flex.orders.exception.OrderNotFoundException;
import com.flex.orders.mapper.OrderMapperImpl;
import com.flex.orders.model.Order;
import com.flex.orders.model.OrderStatus;
import com.flex.orders.service.JwtService;
import com.flex.orders.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
                Order order = new Order("Anthony", 200.0, OrderStatus.SHIPPED);
                Order order2 = new Order("Flex", 5000.0, OrderStatus.PENDING);

                when(orderService.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order, order2)));

                mockMvc.perform(get("/orders"))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.content.length()").value(2),
                                                jsonPath("$.content[0].customerName").value("Anthony"),
                                                jsonPath("$.content[1].customerName").value("Flex"),
                                                jsonPath("$.content[0].amount").value(200.0),
                                                jsonPath("$.content[1].amount").value(5000.0));
        }

        @Test
        void getOrder_shouldReturnOrderById() throws Exception {
                Order order = new Order("Anthony", 200.0, OrderStatus.SHIPPED);

                when(orderService.getById(1)).thenReturn(order);

                mockMvc.perform(get("/orders/1"))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.customerName").value("Anthony"),
                                                jsonPath("$.amount").value(200.0),
                                                jsonPath("$.status").value("SHIPPED"));
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
                Order request = new Order("Claude", 300.0, OrderStatus.PENDING);
                Order response = new Order("Claude", 300.0, OrderStatus.PENDING);

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
                Order request = new Order("", 300.0, OrderStatus.PENDING);

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
                Order request = new Order("Andrenz", 300.0, OrderStatus.PENDING);
                Order response = new Order("King Andrenz", 350.0, OrderStatus.PENDING);

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
