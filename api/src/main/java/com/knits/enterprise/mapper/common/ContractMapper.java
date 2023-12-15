package com.knits.enterprise.mapper.common;

import com.knits.enterprise.model.company.Contract;
import com.knits.enterprise.dto.search.ContractSearchDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper extends EntityMapper<ContractSearchDto, Contract> {
    @Override
    @Mapping(source = "employeeLastName", target = "employee.lastName")
    @Mapping(source = "binaryDataContentType", target = "binaryData.contentType")
    @Mapping(source = "binaryDataTitle", target = "binaryData.title")
    Contract toDto(ContractSearchDto contractSearchDto);

    @InheritInverseConfiguration(name = "toDto")
    @Override

    @Mapping(source = "employee.lastName", target = "employeeLastName")
    @Mapping(source = "binaryData.contentType", target = "binaryDataContentType")
    @Mapping(source = "binaryData.title", target = "binaryDataTitle")
    ContractSearchDto toEntity(Contract contract);
    List<ContractSearchDto> toEntities(List<Contract>contracts);


}