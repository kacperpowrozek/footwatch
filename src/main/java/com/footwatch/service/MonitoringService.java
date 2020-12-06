package com.footwatch.service;

import com.footwatch.model.Monitoring;
import com.footwatch.model.MonitoringPK;
import com.footwatch.model.Player;
import com.footwatch.model.Scout;
import com.footwatch.repository.MonitoringRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    final MonitoringRepository monitoringRepository;

    public MonitoringService(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    public List<Player> getAllMonitoredPlayers(String scoutUsername) {
        return monitoringRepository.findAll().
                stream().filter(m -> m.getScout().getUsername().equals(scoutUsername)).
                filter(Monitoring::isAcceptedByPlayer).map(Monitoring::getPlayer).
                collect(Collectors.toList());
    }

    public List<Scout> getAllMonitoringScouts(String playerUsername) {
        return monitoringRepository.findAll().
                stream().filter(m -> m.getPlayer().getUsername().equals(playerUsername)).
                filter(Monitoring::isAcceptedByPlayer).map(Monitoring::getScout).
                collect(Collectors.toList());
    }

    public List<Scout> getAllScoutsWithPendingRequests(String playerUsername) {
        return monitoringRepository.findAll().
                stream().filter(m -> m.getPlayer().getUsername().equals(playerUsername)).
                filter(m -> !m.isAcceptedByPlayer()).map(Monitoring::getScout).
                collect(Collectors.toList());
    }

    public List<Player> getAllPlayersWithPendingRequest(String scoutUsername) {
        return monitoringRepository.findAll().
                stream().filter(m -> m.getScout().getUsername().equals(scoutUsername)).
                filter(m -> !m.isAcceptedByPlayer()).map(Monitoring::getPlayer).
                collect(Collectors.toList());
    }

    public void deleteMonitoring(Long scoutId, Long playerId) {
        List<Monitoring> monitoringToBeDeleted = monitoringRepository.findAll().stream().
                filter(m -> m.getPlayer().getId().equals(playerId)).
                filter(m -> m.getScout().getId().equals(scoutId)).
                collect(Collectors.toList());
        for (Monitoring m: monitoringToBeDeleted) {
            monitoringRepository.delete(m);
        }
    }

    public Monitoring createNew(Monitoring newMonitoring) {
        return monitoringRepository.save(newMonitoring);
    }

    public boolean exists(Long playerIdLong, Long scoutIdLong) {
        List<Monitoring> monitoringList = monitoringRepository.findAll().stream().
                filter(m -> m.getPlayer().getId().equals(playerIdLong)).
                filter(m -> m.getScout().getId().equals(scoutIdLong)).
                collect(Collectors.toList());
        if(monitoringList.size() == 0) {
            return false;
        }
        else if (monitoringList.size() == 1) {
            return true;
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean acceptMonitoring(Long scoutId, Long playerId) {
        Optional<Monitoring> optional = monitoringRepository.findById(new MonitoringPK(playerId, scoutId));
        Monitoring monitoring = optional.get();
        java.util.Date dateTodayUtil = new java.util.Date();
        java.sql.Date dateTodaySql = new java.sql.Date(dateTodayUtil.getTime());
        if(!monitoring.isAcceptedByPlayer()) {
            monitoring.setStartDate(dateTodaySql);
            monitoring.setAcceptedByPlayer(true);
            monitoringRepository.save(monitoring);
            return true;
        }
        else if (monitoring.isAcceptedByPlayer()) {
            return false;
        }
        else {
            return false;
        }
    }
}
