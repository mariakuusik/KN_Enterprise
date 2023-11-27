package com.knits.enterprise.dto.search;

import com.knits.enterprise.model.company.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class EmployeeSearchDto extends GenericSearchDto<Employee> {

    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Long businessUnitId;
    private Long jobTitleId;
    private Long departmentId;

    protected void addFilters(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {

        if (StringUtils.isNotEmpty(firstName)) {
            Predicate firstNameAsPredicate = criteriaBuilder.equal(root.get("firstName"), firstName);
            filters.add(firstNameAsPredicate);
        }

        if (StringUtils.isNotEmpty(lastName)) {
            Predicate lastNameAsPredicate = criteriaBuilder.equal(root.get("lastName"), lastName);
            filters.add(lastNameAsPredicate);
        }

        if (startDate != null) {
            Predicate startDateAsPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
            filters.add(startDateAsPredicate);
        }

        if (endDate != null) {
            Predicate endDateAsPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
            filters.add(endDateAsPredicate);
        }

        if (businessUnitId != null) {
            Predicate businessUnitIdAsPredicate = criteriaBuilder.equal(root.get("businessUnit").get("id"), businessUnitId);
            filters.add(businessUnitIdAsPredicate);
        }

        if (jobTitleId != null) {
            Predicate jobTitleIdASPredicate = criteriaBuilder.equal(root.get("jobTitle").get("id"), jobTitleId);
            filters.add(jobTitleIdASPredicate);
        }

        if (departmentId != null) {
            Predicate departmentIdAsPredicate = criteriaBuilder.equal(root.get("department").get("id"), departmentId);
            filters.add(departmentIdAsPredicate);
        }
    }


}
