package com.footwatch.service;

import com.footwatch.model.Match;
import com.footwatch.model.MatchEvaluationScout;
import com.footwatch.model.Player;
import com.footwatch.model.Scout;
import com.footwatch.repository.MatchEvaluationScoutRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchEvaluationScoutServiceTest {

    @InjectMocks
    private MatchEvaluationScoutService matchEvaluationScoutServiceWithMocks;

    @Mock
    private MatchEvaluationScoutRepository mockMatchEvaluationScoutRepository;

    Match match;
    Scout scout;
    MatchEvaluationScout matchEvaluationScout;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        Player player = new Player();
        player.setId(1L);
        player.setUsername("player1");
        player.setName("Player1");
        player.setSurname("Player1");
        player.setEmail("player1@footwatch.pl");
        match = new Match();
        match.setId(1L);
        match.setHomeTeam("team1");
        match.setAwayTeam("team2");
        match.setPlayer(player);
        scout = new Scout();
        scout.setId(1L);
        scout.setUsername("scout1");
        scout.setName("Scout1");
        scout.setSurname("Scout1");
        scout.setEmail("scout1@footwatch.pl");
        matchEvaluationScout = new MatchEvaluationScout();
        matchEvaluationScout.setId(1L);
        matchEvaluationScout.setEvaluationVerbal("test evaluation verbal");
        matchEvaluationScout.setGoals(2);
        matchEvaluationScout.setPasses(33);
        matchEvaluationScout.setMatch(match);
        matchEvaluationScout.setScout(scout);
        ArrayList<MatchEvaluationScout> matchEvaluationScoutArrayList = new ArrayList<>();
        matchEvaluationScoutArrayList.add(matchEvaluationScout);
        Mockito.when(mockMatchEvaluationScoutRepository.findAll()).thenReturn(matchEvaluationScoutArrayList);
        Mockito.when(mockMatchEvaluationScoutRepository.save(matchEvaluationScout)).thenReturn(matchEvaluationScout);
    }

    @Test
    void findOne() {
        MatchEvaluationScout matchEvaluationScout = matchEvaluationScoutServiceWithMocks.findOne(match, scout);
        assertThat(matchEvaluationScout).isNotNull();
        assertThat(matchEvaluationScout.getId()).isEqualTo(1L);
        assertThat(matchEvaluationScout.getEvaluationVerbal()).isEqualTo("test evaluation verbal");
        assertThat(matchEvaluationScout.getGoals()).isEqualTo(2);
        assertThat(matchEvaluationScout.getPasses()).isEqualTo(33);
        assertThat(matchEvaluationScout.getMatch()).isEqualTo(match);
        assertThat(matchEvaluationScout.getScout()).isEqualTo(scout);
    }

    @Test
    void createNew() {
        MatchEvaluationScout createdMatchEvaluationScout = matchEvaluationScoutServiceWithMocks.createNew(matchEvaluationScout);
        assertThat(createdMatchEvaluationScout).isSameAs(matchEvaluationScout);
    }
}