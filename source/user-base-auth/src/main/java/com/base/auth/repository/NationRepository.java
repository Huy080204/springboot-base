package com.base.auth.repository;

import com.base.auth.model.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
    Nation findByName(String name);
}
