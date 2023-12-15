package com.knits.enterprise.repository.common;

import com.knits.enterprise.model.common.BinaryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BinaryDataRepository extends JpaRepository<BinaryData, Long> {


}