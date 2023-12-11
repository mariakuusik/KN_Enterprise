package com.knits.enterprise.service.company;

import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.model.company.Group;
import com.knits.enterprise.repository.company.EmployeeRepository;
import com.knits.enterprise.repository.company.GroupRepository;
import com.knits.enterprise.utils.Constant;
import com.knits.enterprise.utils.Report;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeGroupService {

    private final GroupRepository groupRepository;
    private final EmployeeRepository employeeRepository;

    public List<Report<Employee>> addEmployeesToGroup(Long groupId, List<Long> employeeIds) {

        Group groupWithEmployees = getGroupWithEmployees(groupId);
        List<Report<Employee>> reports = new ArrayList<>();
        List<Employee> employees = getEmployeeList(employeeIds, reports);

        if (employees.isEmpty()) {
            throw new UserException("No users were found with these IDs");
        }
        checkAndAddEmployeesToGroup(employees, groupWithEmployees, reports);

        //ManyToMany relation in Group Entity triggers JPA to save foreign key values to employees_groups table
        groupRepository.save(groupWithEmployees);
        return reports;
    }

    private Group getGroupWithEmployees(Long groupId) {
        return groupRepository.findByIdWithEmployees(groupId).orElseThrow(()
                -> new UserException("Group with ID " + groupId + " does not exist"));
    }

    private List<Employee> getEmployeeList(List<Long> employeeIds, List<Report<Employee>> reports) {
        List<Employee>employees = new ArrayList<>();
        for (Long employeeId : employeeIds) {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            if (employee != null) {
                employees.add(employee);
            } else {
                reports.add(new Report<>(employeeId, Integer.parseInt(Constant.CODE200), Constant.NOT_EXIST));
            }
        }
        return employees;
    }

    private static void checkAndAddEmployeesToGroup(List<Employee> employees, Group groupWithEmployees, List<Report<Employee>> reports) {
        for (Employee employee : employees) {
            if (!groupWithEmployees.getEmployees().contains(employee)) {
                groupWithEmployees.getEmployees().add(employee);
                reports.add(new Report<>(employee.getId(), Integer.parseInt(Constant.CODE1024), Constant.INSERTED));
            } else {
                reports.add(new Report<>(employee.getId(), Integer.parseInt(Constant.CODE100), Constant.DUPLICATE));
            }
        }
    }
}
