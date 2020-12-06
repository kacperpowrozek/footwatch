package com.footwatch.service;

import com.footwatch.model.Scout;
import com.footwatch.repository.ScoutRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ScoutService {

    final ScoutRepository scoutRepository;

    public ScoutService(ScoutRepository scoutRepository) {
        this.scoutRepository = scoutRepository;
    }

    public Scout getOne(Long id) {
        Optional<Scout> optional = scoutRepository.findById(id);
        return optional.orElse(null);
    }

    public Scout createNew(Scout newScout) {
        return scoutRepository.save(newScout);
    }

    public boolean exists(String username) {
        List<Scout> allScouts = scoutRepository.findAll();
        for (int i = 0; i < allScouts.size(); i++) {
            Scout currentScout = allScouts.get(i);
            if(currentScout.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Scout findByUsername(String username) {
        List<Scout> allScouts = scoutRepository.findAll();
        for (int i = 0; i < allScouts.size(); i++) {
            Scout currentScout = allScouts.get(i);
            if (currentScout.getUsername().equals(username)) {
                return currentScout;
            }
        }
        return null;
    }
}