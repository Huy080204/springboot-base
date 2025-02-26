package com.base.auth.mapper;

import com.base.auth.dto.product.ProductDto;
import com.base.auth.form.product.CreateProductForm;
import com.base.auth.form.product.UpdateProductForm;
import com.base.auth.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface ProductMapper {

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "sellOff", target = "sellOff")
    @Mapping(source = "image", target = "image")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToDto")
    ProductDto fromEntityToDto(Product product);

    @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "fromEntityToDto")
    List<ProductDto> fromEntityToDtoList(List<Product> products);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "sellOff", target = "sellOff")
    @Mapping(source = "image", target = "image")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateProductFormToEntity")
    Product fromCreateProductFormToEntity(CreateProductForm createProductForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "sellOff", target = "sellOff")
    @Mapping(source = "image", target = "image")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateProductFromUpdateProductForm")
    void updateProductFromUpdateProductForm(UpdateProductForm updateProductForm, @MappingTarget Product product);
}