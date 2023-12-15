package com.knits.enterprise.repository.common;

import com.knits.enterprise.model.company.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
    List<Contract> findByEmployeeId(Long employeeId);

    @Query("select c from Contract c where c.employee.id = ?1 and c.active = ?2")
    Contract findByEmployeeIdAndIsActive(Long id, Boolean active);

    @Query("select (count(c) > 0) from Contract c where c.binaryData.title = ?1")
    boolean existsByBinaryDataTitle(String title);


}
