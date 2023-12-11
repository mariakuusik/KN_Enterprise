package com.knits.enterprise.controller.company;

import com.knits.enterprise.model.company.Group;
import com.knits.enterprise.service.company.EmployeeGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor(onConstructor_=@Autowired)
public class EmployeeGroupController {
    private final EmployeeGroupService employeeGroupService;

    @PostMapping(value = "/employees/group")
    public void addEmployeesToGroup(@RequestParam Long groupId, @RequestBody List<Long> employeeIds) {
        Group group = employeeGroupService.addEmployeesToGroup(groupId, employeeIds);
    }

}


