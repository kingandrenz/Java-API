package com.flex.orders.mapper;

import com.flex.orders.model.Order;
import com.flex.orders.dto.OrderRequestDto;
import com.flex.orders.dto.OrderResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequestDto dto);

    OrderResponseDto toDto(Order order);
}
