package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.dto.search.EmployeeSearchDto;
import com.knits.enterprise.excel.EmployeeExcelGenerator;
import com.knits.enterprise.excel.EmployeeExcelImporter;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.service.company.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeExcelGenerator employeeExcelGenerator;
    private final EmployeeExcelImporter employeeExcelImporter;
    private final EmployeeMapper employeeMapper;

    @PostMapping(value = "/employees", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto) {
        log.debug("REST request to create Employee");
        return ResponseEntity
                .ok()
                .body(employeeService.saveNewEmployee(employeeDto));
    }

    @GetMapping(value = "/employees/{id}", produces = {"application/json"})
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to get Employee : {}", id);
        EmployeeDto employeeFound = employeeService.findEmployeeById(id);
        return ResponseEntity
                .ok()
                .body(employeeFound);
    }

    @PatchMapping(value = "/employees", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable(value = "id") @RequestBody EmployeeDto employeeDto) {
        EmployeeDto employeeFound = employeeService.partialUpdate(employeeDto);
        return ResponseEntity
                .ok()
                .body(employeeFound);
    }

    @PutMapping(value = "/employees/{id}", produces = {"application/json"})
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to delete Employee : {}", id);
        EmployeeDto employeeFound = employeeService.deleteEmployee(id);
        return ResponseEntity
                .ok()
                .body(employeeFound);
    }

    @GetMapping(value = "/employees")
    @Operation(summary = "Searches for employees by filters",
            description = """
                        all combinations of filters are supported, including NONE and ALL selected,
                        for foreign key entities only ID is provided as input.
                    """)
    public ResponseEntity<PaginatedResponseDto<List<EmployeeDto>>> findEmployees(EmployeeSearchDto employeeSearchDto) {
        PaginatedResponseDto responseDto = employeeService.filterEmployees(employeeSearchDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/employees/xls", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Operation(summary = "Searches for employees by filters, returns data as Excel file")
    public ResponseEntity<byte[]> findEmployeesAndReturnExcelFile(EmployeeSearchDto employeeSearchDto) throws IOException {
        PaginatedResponseDto<List<EmployeeDto>> paginatedResponseDto = employeeService.filterEmployees(employeeSearchDto);

        //Unwrap the inner list from paginated response
        List<EmployeeDto> employeeDtos = paginatedResponseDto.getData().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        //Convert list EmployeeDto to list of Employee entities
        List<Employee> employees = employeeDtos.stream()
                .map(employeeMapper::toEntity)
                .collect(Collectors.toList());
        return generateExcelFileWithFilteredResults(employees);
    }

    private ResponseEntity<byte[]> generateExcelFileWithFilteredResults(List<Employee> employees) throws IOException {
        byte[] excelFile = employeeExcelGenerator.generateExcelFile(employees);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employees.xlsx")
                .body(excelFile);
    }

    @PostMapping(value = "/employees/xls")
    @Operation(summary = "Creates new employee, input as excel file")
    public ResponseEntity <List<EmployeeDto>> createNewEmployeeFromExcelFile(@RequestParam("file") MultipartFile excelFile) throws IOException {
        List<EmployeeDto> employeeDtos = employeeExcelImporter.importEmployees(excelFile);
        return ResponseEntity
                .ok()
                .body(employeeDtos);
    }

}
