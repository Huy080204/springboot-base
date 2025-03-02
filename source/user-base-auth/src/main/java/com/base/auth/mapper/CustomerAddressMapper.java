package com.base.auth.mapper;

import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.dto.customerAddress.CustomerAddressDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.form.customerAddress.CreateCustomerAddressForm;
import com.base.auth.form.customerAddress.UpdateCustomerAddressForm;
import com.base.auth.model.Customer;
import com.base.auth.model.CustomerAddress;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {NationMapper.class})
public interface CustomerAddressMapper {

    @Mapping(source = "address", target = "address")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "defaultAddress", target = "defaultAddress")
    @Named("fromCreateCustomerAddress")
    @BeanMapping(ignoreByDefault = true)
    CustomerAddress fromCreateCustomerAddress(CreateCustomerAddressForm createCustomerAddressForm);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "defaultAddress", target = "defaultAddress")
    @Named("mappingForUpdateCustomerAddress")
    @BeanMapping(ignoreByDefault = true)
    void mappingForUpdateCustomerAddress(UpdateCustomerAddressForm updateCustomerAddressForm, @MappingTarget CustomerAddress customerAddress);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer.account.username", target = "username")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromNationToDto")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromNationToDto")
    @Mapping(source = "commune", target = "commune", qualifiedByName = "fromNationToDto")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "defaultAddress", target = "defaultAddress")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCustomerAddressDto")
    CustomerAddressDto fromEntityToCustomerAddressDto(CustomerAddress customerAddress);

    @IterableMapping(elementTargetType = CustomerAddressDto.class, qualifiedByName = "fromEntityToCustomerAddressDto")
    @Named("fromEntityToCustomerAddressDtoList")
    List<CustomerAddressDto> fromEntityToCustomerAddressDtoList(List<CustomerAddress> customerAddresses);
}
