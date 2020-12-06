package com.footwatch.repository;

import com.footwatch.model.Monitoring;
import com.footwatch.model.MonitoringPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringRepository extends JpaRepository<Monitoring, MonitoringPK> { // check if monitoring problems
    // were related with Long here instead of MonitoringPL !!!

}
