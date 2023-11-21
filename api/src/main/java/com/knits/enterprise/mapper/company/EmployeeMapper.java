package com.knits.enterprise.mapper.company;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.mapper.common.EntityMapper;
import com.knits.enterprise.mapper.common.OrganizationMapper;
import com.knits.enterprise.mapper.location.LocationMapper;

import com.knits.enterprise.model.company.Employee;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrganizationMapper.class, DivisionMapper.class, BusinessUnitMapper.class,
                CostCenterMapper.class, GroupMapper.class, JobTitleMapper.class, TeamMapper.class,
                DepartmentMapper.class, LocationMapper.class})
public interface EmployeeMapper extends EntityMapper<Employee, EmployeeDto> {

    @Override
    @Mapping(source = "birthDate", target = "birthDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "startDate", target = "startDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "endDate", target = "endDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(target = "solidLineManager", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "jobTitle", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "division", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EmployeeDto toDto(Employee entity);

    @Override
    @Mapping(source = "birthDate", target = "birthDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "startDate", target = "startDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "endDate", target = "endDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(target = "solidLineManager", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "jobTitle", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "division", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee toEntity(EmployeeDto dto);

    @Mapping(source = "birthDate", target = "birthDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "startDate", target = "startDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "endDate", target = "endDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(target = "solidLineManager", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "jobTitle", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "division", ignore = true)
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<Employee> toEntities(List<EmployeeDto> entityList);

    List<EmployeeDto> toDtos(List<Employee>list);
}
