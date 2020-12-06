package com.footwatch.repository;

import com.footwatch.model.Scout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoutRepository extends JpaRepository<Scout, Long> {
}
