package com.knits.enterprise.controller.company;

import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.service.company.EmployeeGroupService;
import com.knits.enterprise.utils.Report;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EmployeeGroupController {
    private final EmployeeGroupService employeeGroupService;

    @PostMapping(value = "/employees/group")
    @Operation(summary = "Adds Employee(s) to existing group, returns Report for each Employee added")
    public ResponseEntity<List<Report<Employee>>> addEmployeesToGroup(
            @RequestParam Long groupId,
            @RequestBody List<Long> employeeIds) {
        List<Report<Employee>> reports = employeeGroupService.addEmployeesToGroup(groupId, employeeIds);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @DeleteMapping(value = "/employees/group")
    @Operation(summary = "Removes Employee(s) from existing group")
    public ResponseEntity<String> removeEmployeesFromGroup(
            @RequestParam Long groupId,
            @RequestBody List<Long> employeeIds) {
        employeeGroupService.removeEmployeesFromGroup(groupId, employeeIds);
        return ResponseEntity.ok().body("Employee(s) removed from group");
    }
}


