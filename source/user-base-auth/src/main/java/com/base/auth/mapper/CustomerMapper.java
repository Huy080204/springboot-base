package com.base.auth.mapper;

import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.model.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, NationMapper.class})
public interface CustomerMapper {

    @Mapping(source = "birthDay", target = "birthDay")
    @Mapping(source = "gender", target = "gender")
    @Named("fromCreateCustomer")
    @BeanMapping(ignoreByDefault = true)
    Customer fromCreateCustomer(CreateCustomerForm createCustomerForm);

    @Mapping(source = "birthDay", target = "birthDay")
    @Mapping(source = "gender", target = "gender")
    @Named("mappingForUpdateCustomer")
    @BeanMapping(ignoreByDefault = true)
    void mappingForUpdateCustomer(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromAccountToDto")
    @Mapping(source = "birthDay", target = "birthDay")
    @Mapping(source = "gender", target = "gender")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCustomerDto")
    CustomerDto fromCustomerToDto(Customer customer);

}
