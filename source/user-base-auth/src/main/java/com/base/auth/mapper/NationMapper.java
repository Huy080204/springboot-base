package com.base.auth.mapper;

import com.base.auth.dto.nation.NationDto;
import com.base.auth.form.nation.CreateNationForm;
import com.base.auth.form.nation.UpdateNationForm;
import com.base.auth.model.Nation;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NationMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "type", target = "type")
    @Named("fromCreateNation")
    @BeanMapping(ignoreByDefault = true)
    Nation fromCreateNation(CreateNationForm createNationForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "type", target = "type")
    @Named("mappingForUpdateNation")
    @BeanMapping(ignoreByDefault = true)
    void mappingForUpdateNation(UpdateNationForm updateNationForm, @MappingTarget Nation nation);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "type", target = "type")
    @Named("fromNationToDto")
    NationDto fromNationToDto(Nation nation);

}
