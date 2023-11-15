package com.knits.enterprise.dto.search;

import com.knits.enterprise.dto.search.GenericSearchDto;
import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.model.security.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.knits.enterprise.model.company.Team}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamSearchDto extends GenericSearchDto<Team> {
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime endDate;
    private String createdByLogin;

    protected void addFilters(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {

        if (StringUtils.isNotEmpty(name)) {
            Predicate teamNameAsPredicate = criteriaBuilder.equal(root.get("name"), name);
            filters.add(teamNameAsPredicate);
        }

        if (startDate!=null) {
            Predicate startDateAsPredicate = criteriaBuilder.equal(root.get("startDate"), startDate);
            filters.add(startDateAsPredicate);
        }

        if (endDate!=null) {
            Predicate endDateAsAPredicate = criteriaBuilder.equal(root.get("endDate"), endDate);
            filters.add(endDateAsAPredicate);
        }

        if (StringUtils.isNotEmpty(createdByLogin)) {
            Predicate createdByLoginAsPredicate = criteriaBuilder.equal(root.get("createdBy").get("login"), createdByLogin);
            filters.add(createdByLoginAsPredicate);
        }
    }
}