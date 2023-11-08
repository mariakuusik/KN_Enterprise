package com.knits.enterprise.repository.company;

import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.repository.common.ActiveEntityRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends ActiveEntityRepository<Team> {
    @Transactional
    @Modifying
    @Query("update Team t set t.active = ?1")
    int updateActiveBy(boolean active);

    Optional<Team> findByName(String name);

    Boolean existsByName(String name);

    @Query("select t from Team t order by t.name")
    List<Team> findAllIncludingActive();


}
