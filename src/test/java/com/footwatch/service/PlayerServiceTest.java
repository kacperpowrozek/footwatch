package com.footwatch.service;

import com.footwatch.model.Player;
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
class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerServiceWithMocks;

    @Mock
    private PlayerRepository mockPlayerRepository;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        ArrayList<Player> playerArrayList = new ArrayList<>();
        Player player = new Player();
        player.setId(1L);
        player.setUsername("player1");
        player.setName("Player1");
        player.setSurname("Player1");
        player.setEmail("player1@footwatch.pl");
        playerArrayList.add(player);
        Mockito.when(mockPlayerRepository.findAll()).thenReturn(playerArrayList);
        Mockito.when(mockPlayerRepository.findById(1L)).thenReturn(Optional.of(player));
    }

    @Test
    void getOne() {
        Player player = playerServiceWithMocks.getOne(1L);
        assertThat(player).isNotNull();
        assertThat(player.getId()).isEqualTo(1L);
        assertThat(player.getUsername()).isEqualTo("player1");
    }

    @Test
    void createNew() {
        Player player = new Player();
        player.setEmail("test@footwatch.pl");
        player.setName("Foo");
        player.setSurname("Surname");
        Mockito.when(mockPlayerRepository.save(player)).thenReturn(player);
        Player createdPlayer = playerServiceWithMocks.createNew(player);
        assertThat(createdPlayer).isSameAs(player);
    }

    @Test
    void exists() {
        boolean playerExists = playerServiceWithMocks.exists("player1");
        assertThat(playerExists).isEqualTo(true);
    }

    @Test
    void findByUsername() {
        Player player = playerServiceWithMocks.findByUsername("player1");
        assertThat(player).isNotNull();
        assertThat(player.getUsername()).isEqualTo("player1");
        assertThat(player.getEmail()).isEqualTo("player1@footwatch.pl");
    }

    @Test
    void findPlayersByNameAndSurname() {
        List<Player> playersList = playerServiceWithMocks.findPlayersByNameAndSurname("Player1",
                                                                                "Player1");
        assertThat(playersList).isNotNull();
        assertThat(playersList.get(0)).isNotNull();
        assertThat(playersList.get(0).getName()).isEqualTo("Player1");
        assertThat(playersList.get(0).getSurname()).isEqualTo("Player1");
    }
}