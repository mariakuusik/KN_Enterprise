package com.knits.enterprise.repository.common;

import com.knits.enterprise.model.company.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
    List<Contract> findByEmployee_Id(Long employeeId);

    @Query("select c from Contract c where c.employee.id = ?1 and c.active = ?2")
    Contract findByEmployee_IdAndActive(Long id, Boolean active);
}
