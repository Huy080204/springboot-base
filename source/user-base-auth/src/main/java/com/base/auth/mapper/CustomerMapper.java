package com.base.auth.mapper;

import com.base.auth.dto.category.CategoryDto;
import com.base.auth.form.category.CreateCategoryForm;
import com.base.auth.form.category.UpdateCategoryForm;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.model.Category;
import com.base.auth.model.Customer;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "birthDay", target = "birthDay")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "address", target = "address")
    @Named("fromCreateCustomer")
    @BeanMapping(ignoreByDefault = true)
    Customer fromCreateCustomer(CreateCustomerForm createCustomerForm);

    @Mapping(source = "birthDay", target = "birthDay")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "address", target = "address")
    @Named("mappingForUpdateCustomer")
    @BeanMapping(ignoreByDefault = true)
    void mappingForUpdateCustomer(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

}
