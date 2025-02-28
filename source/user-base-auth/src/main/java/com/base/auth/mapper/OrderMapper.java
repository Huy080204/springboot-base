package com.base.auth.mapper;

import com.base.auth.dto.order.OrderDto;
import com.base.auth.dto.orderItem.OrderItemDto;
import com.base.auth.model.Order;
import com.base.auth.model.OrderItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrderItemMapper.class, CustomerMapper.class}
)

public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "totalMoney", target = "totalMoney")
    @Mapping(source = "totalSellOff", target = "totalSellOff")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "fromEntityToCustomerDto")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "fromEntityToOrderItemDtoList")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToOrderDto")
    OrderDto fromEntityToOrderDto(Order order);

    @IterableMapping(qualifiedByName = "fromEntityToOrderDto")
    @Named("fromEntityToOrderDtoList")
    List<OrderDto> fromEntityToOrderDtoList(List<Order> orders);

}
