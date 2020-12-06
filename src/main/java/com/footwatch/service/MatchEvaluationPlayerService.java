package com.footwatch.service;

import com.footwatch.model.Match;
import com.footwatch.model.MatchEvaluationPlayer;
import com.footwatch.repository.MatchEvaluationPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchEvaluationPlayerService {

    final MatchEvaluationPlayerRepository matchEvaluationPlayerRepository;

    public MatchEvaluationPlayerService(MatchEvaluationPlayerRepository matchEvaluationPlayerRepository) {

        this.matchEvaluationPlayerRepository = matchEvaluationPlayerRepository;
    }

    public MatchEvaluationPlayer createNew(MatchEvaluationPlayer newMatchEvaluationPlayer) {
        return matchEvaluationPlayerRepository.save(newMatchEvaluationPlayer);
    }

    public MatchEvaluationPlayer findByMatch(Match match) {
        List<MatchEvaluationPlayer> matchEvaluationPlayerList = matchEvaluationPlayerRepository.findAll().stream().
                filter(mep -> mep.getMatch().equals(match)).collect(Collectors.toList());
        if(matchEvaluationPlayerList.size() == 1) {
            return matchEvaluationPlayerList.get(0);
        }
        else {
            throw new UnsupportedOperationException();
        }
    }
}
