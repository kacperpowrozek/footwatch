package com.footwatch.service;

import com.footwatch.model.Match;
import com.footwatch.model.Player;
import com.footwatch.repository.MatchRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match createNew(Match newMatch) {
        return matchRepository.save(newMatch);
    }

    public Match getMatchById(Long matchId) {
        Optional<Match> optional = matchRepository.findById(matchId);
        return optional.orElse(null);
    }

    public List<Match> getAllMatchesOfPlayer(Player player) {
        return matchRepository.findAll().stream().
                filter(m -> m.getPlayer().getId().equals(player.getId())).
                collect(Collectors.toList());
    }
}
