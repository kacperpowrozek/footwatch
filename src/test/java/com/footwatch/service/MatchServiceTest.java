package com.footwatch.service;

import com.footwatch.model.Match;
import com.footwatch.model.Player;
import com.footwatch.repository.MatchRepository;
import com.footwatch.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchServiceTest {

    @InjectMocks
    private MatchService matchServiceWithMocks;

    @Mock
    private MatchRepository mockMatchRepository;

    Player player = new Player();

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        player.setId(1L);
        player.setUsername("player1");
        player.setName("Player1");
        player.setSurname("Player1");
        player.setEmail("player1@footwatch.pl");
        ArrayList<Match> matchArrayList = new ArrayList<>();
        Match match = new Match();
        match.setId(1L);
        match.setHomeTeam("team1");
        match.setAwayTeam("team2");
        match.setPlayer(player);
        matchArrayList.add(match);
        Mockito.when(mockMatchRepository.findAll()).thenReturn(matchArrayList);
        Mockito.when(mockMatchRepository.findById(1L)).thenReturn(Optional.of(match));
    }

    @Test
    void createNew() {
        Match match = new Match();
        match.setHomeTeam("test1");
        match.setHomeTeam("test2");
        Player player = new Player();
        player.setId(1L);
        player.setUsername("player1");
        match.setPlayer(player);
        Mockito.when(mockMatchRepository.save(match)).thenReturn(match);
        Match createdMatch = matchServiceWithMocks.createNew(match);
        assertThat(createdMatch).isSameAs(match);
    }

    @Test
    void getMatchById() {
        Match match = matchServiceWithMocks.getMatchById(1L);
        assertThat(match).isNotNull();
        assertThat(match.getId()).isEqualTo(1L);
        assertThat(match.getHomeTeam()).isEqualTo("team1");
        assertThat(match.getAwayTeam()).isEqualTo("team2");
        assertThat(match.getPlayer()).isEqualTo(player);
    }

    @Test
    void getAllMatchesOfPlayer() {
        List<Match> matchesOfPlayer = matchServiceWithMocks.getAllMatchesOfPlayer(player);
        assertThat(matchesOfPlayer.get(0).getId()).isEqualTo(1L);
        assertThat(matchesOfPlayer.get(0).getHomeTeam()).isEqualTo("team1");
        assertThat(matchesOfPlayer.get(0).getAwayTeam()).isEqualTo("team2");
        assertThat(matchesOfPlayer.get(0).getPlayer()).isEqualTo(player);
    }
}