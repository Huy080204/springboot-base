package com.base.auth.mapper;

import com.base.auth.dto.orderItem.OrderItemDto;
import com.base.auth.model.OrderItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface OrderItemMapper {

    @Mapping(source = "id", target = "orderItemId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "singlePrice", target = "singlePrice")
    @Mapping(source = "sellOff", target = "sellOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToOrderItemDto")
    OrderItemDto fromEntityToOrderItemDto(OrderItem orderItem);

    @IterableMapping(qualifiedByName = "fromEntityToOrderItemDto")
    @Named("fromEntityToOrderItemDtoList")
    List<OrderItemDto> fromEntityToOrderItemDtoList(List<OrderItem> orderItems);

}
