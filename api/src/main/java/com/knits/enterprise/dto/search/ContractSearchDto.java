package com.knits.enterprise.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.knits.enterprise.config.Constants;
import com.knits.enterprise.model.common.BinaryData;
import com.knits.enterprise.model.company.Contract;
import com.knits.enterprise.model.company.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Contract}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"page", "limit", "sort", "dir"}, allowSetters = true)
public class ContractSearchDto extends GenericSearchDto<Contract> {
    private String binaryDataTitle;
    private String binaryDataContentType;
    private String employeeLastName;
    @DateTimeFormat(pattern = Constants.TIME_FORMAT_DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = Constants.TIME_FORMAT_DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime createdAtFrom;
    @DateTimeFormat(pattern = Constants.TIME_FORMAT_DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime createdAtTo;

    @Override
    protected void addFilters(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {
        if (StringUtils.isNotEmpty(binaryDataTitle)) {
            Predicate binaryDataTitleAsPredicate = criteriaBuilder.equal(root.get("binaryDataTitle"), binaryDataTitle);
            filters.add(binaryDataTitleAsPredicate);
        }
        if (StringUtils.isNotEmpty(binaryDataContentType)) {
            Join<Contract, BinaryData> binaryDataJoin = root.join("binaryData", JoinType.INNER);
            Predicate binaryDataContentTypeAsPredicate = criteriaBuilder.equal(binaryDataJoin.get("contentType"), binaryDataContentType);
            filters.add(binaryDataContentTypeAsPredicate);
        }
        if (StringUtils.isNotEmpty(employeeLastName)) {
            Join<Contract, Employee> employeeJoin = root.join("employee", JoinType.INNER);
            Predicate employeeLastNameAsPredicate = criteriaBuilder.equal(employeeJoin.get("lastName"), employeeLastName);
            filters.add(employeeLastNameAsPredicate);
        }
        if (createdAt != null) {
            Predicate createdAtAsPredicate = criteriaBuilder.equal(root.get("createdAt"), createdAt);
            filters.add(createdAtAsPredicate);
        }
        if (createdAtFrom != null && createdAtTo != null) {
            Predicate createdAtRangePredicate = criteriaBuilder.between(root.get("createdAt"), createdAtFrom, createdAtTo);
            filters.add(createdAtRangePredicate);
        } else if (createdAtFrom != null) {
            Predicate createdAtFromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom);
            filters.add(createdAtFromPredicate);
        } else if (createdAtTo != null) {
            Predicate createdAtToPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtTo);
            filters.add(createdAtToPredicate);
        }
    }

}