package com.footwatch.service;

import com.footwatch.model.*;
import com.footwatch.repository.MatchEvaluationPlayerRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchEvaluationPlayerServiceTest {

    @InjectMocks
    private MatchEvaluationPlayerService matchEvaluationPlayerServiceWithMocks;

    @Mock
    private MatchEvaluationPlayerRepository mockMatchEvaluationPlayerRepository;

    Match match;
    MatchEvaluationPlayer matchEvaluationPlayer;

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
        matchEvaluationPlayer = new MatchEvaluationPlayer();
        matchEvaluationPlayer.setId(1L);
        matchEvaluationPlayer.setEvaluationVerbal("test evaluation verbal");
        matchEvaluationPlayer.setEvaluationEngagement(5);
        matchEvaluationPlayer.setEvaluationTacticalDiscipline(2);
        matchEvaluationPlayer.setMatch(match);
        ArrayList<MatchEvaluationPlayer> matchEvaluationPlayerArrayList = new ArrayList<>();
        matchEvaluationPlayerArrayList.add(matchEvaluationPlayer);
        Mockito.when(mockMatchEvaluationPlayerRepository.findAll()).thenReturn(matchEvaluationPlayerArrayList);
        Mockito.when(mockMatchEvaluationPlayerRepository.save(matchEvaluationPlayer)).thenReturn(matchEvaluationPlayer);
    }

    @Test
    void createNew() {
        MatchEvaluationPlayer createdMatchEvaluationPlayer = matchEvaluationPlayerServiceWithMocks.createNew(matchEvaluationPlayer);
        assertThat(createdMatchEvaluationPlayer).isSameAs(matchEvaluationPlayer);
    }

    @Test
    void findByMatch() {
        MatchEvaluationPlayer matchEvaluationPlayer = matchEvaluationPlayerServiceWithMocks.findByMatch(match);
        assertThat(matchEvaluationPlayer).isNotNull();
        assertThat(matchEvaluationPlayer.getId()).isEqualTo(1L);
        assertThat(matchEvaluationPlayer.getEvaluationVerbal()).isEqualTo("test evaluation verbal");
        assertThat(matchEvaluationPlayer.getEvaluationTacticalDiscipline()).isEqualTo(2);
        assertThat(matchEvaluationPlayer.getMatch()).isEqualTo(match);
    }
}