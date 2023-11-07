package com.knits.enterprise.repository.company;

import com.knits.enterprise.model.company.Team;
import com.knits.enterprise.repository.common.ActiveEntityRepository;

import java.util.Optional;

public interface TeamRepository extends ActiveEntityRepository<Team> {
    Optional<Team> findByName(String name);
    Boolean existsByName(String name);

}
