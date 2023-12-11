package com.knits.enterprise.service.company;

import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.model.company.Group;
import com.knits.enterprise.repository.company.EmployeeRepository;
import com.knits.enterprise.repository.company.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeGroupService {

    private final GroupRepository groupRepository;
    private final EmployeeRepository employeeRepository;

    public Group addEmployeesToGroup(Long groupId, List<Long> employeeIds) {
        Group groupWithEmployees = groupRepository.findByIdWithEmployees(groupId).orElseThrow(()
                -> new UserException("Group with ID " + groupId + " does not exist"));

        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        if (employees.isEmpty()) {
            throw new UserException("No users were found with these IDs");
        }

        //ManyToMany relation in Group Entity triggers JPA to save foreign key values to employees_groups table
        groupWithEmployees.getEmployees().addAll(employees);
        return groupRepository.save(groupWithEmployees);

    }
}
