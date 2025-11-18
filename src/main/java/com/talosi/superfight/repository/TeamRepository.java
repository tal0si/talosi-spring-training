package com.talosi.superfight.repository;

import com.talosi.superfight.model.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {
    public TeamEntity findByName(String name);
}
