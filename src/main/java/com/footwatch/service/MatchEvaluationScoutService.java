package com.footwatch.service;

import com.footwatch.model.*;
import com.footwatch.repository.MatchEvaluationScoutRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MatchEvaluationScoutService {

    final MatchEvaluationScoutRepository matchEvaluationScoutRepository;

    public MatchEvaluationScoutService(MatchEvaluationScoutRepository matchEvaluationScoutRepository) {
        this.matchEvaluationScoutRepository = matchEvaluationScoutRepository;
    }


    public MatchEvaluationScout findOne(Match match, Scout scout) {
        Optional<MatchEvaluationScout> matchEvaluationScoutOptional = matchEvaluationScoutRepository.findAll().stream().
                filter(mes -> mes.getMatch().equals(match)).
                filter(mes -> mes.getScout().equals(scout)).
                findAny();
        return matchEvaluationScoutOptional.orElse(null);
    }

    public MatchEvaluationScout createNew(MatchEvaluationScout newMatchEvaluationScout) {
        return matchEvaluationScoutRepository.save(newMatchEvaluationScout);
    }
}
