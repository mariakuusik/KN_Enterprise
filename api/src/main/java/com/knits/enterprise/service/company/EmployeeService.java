package com.knits.enterprise.service.company;


import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.dto.search.EmployeeSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.repository.company.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDto saveNewEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee savedEmployee =employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee );
    }

    @Transactional
    public EmployeeDto findEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new UserException("User#" + id + " not found"));
        return employeeMapper.toDto(employee);
    }

    @Transactional
    public EmployeeDto partialUpdate(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeDto.getId()).orElseThrow(() -> new UserException("User#" + employeeDto.getId() + " not found"));
        employeeMapper.partialUpdate(employee, employeeDto);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public PaginatedResponseDto<List<EmployeeDto>> filterEmployees(EmployeeSearchDto employeeSearchDto) {
        Page<Employee> employeePage = employeeRepository.findAll
                (employeeSearchDto.getSpecification(), employeeSearchDto.getPageable());

        List<EmployeeDto> employeeDtos = employeeMapper.toDtos(employeePage.getContent());

        return PaginatedResponseDto.<List<EmployeeDto>>builder()
                .page(employeeSearchDto.getPage())
                .size(employeeDtos.size())
                .sortingFields(employeeSearchDto.getSort())
                .sortDirection(employeeSearchDto.getDir().name())
                .data(Collections.singletonList(employeeDtos))
                .build();
    }

    public List<String> getEntityNamesForEmployee() {
        List<String> entityNames = new ArrayList<>();
        Class<Employee> employeeClass = Employee.class;
        Field[] declaredFields = employeeClass.getDeclaredFields();
        for (Field field : declaredFields) {
            entityNames.add(field.getName());
        }
        return entityNames;
    }
}


