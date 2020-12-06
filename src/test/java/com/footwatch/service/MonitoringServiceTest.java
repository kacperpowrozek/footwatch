package com.footwatch.service;

import com.footwatch.model.Monitoring;
import com.footwatch.model.MonitoringPK;
import com.footwatch.model.Player;
import com.footwatch.model.Scout;
import com.footwatch.repository.MonitoringRepository;
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
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MonitoringServiceTest {

    @InjectMocks
    private MonitoringService monitoringServiceWithMocks;

    @Mock
    private MonitoringRepository mockMonitoringRepository;

    ArrayList<Monitoring> monitoringArrayList = new ArrayList<>();
    Player player1 = new Player();
    Scout scout1 = new Scout();
    Monitoring monitoring1 = new Monitoring();
    Player player2 = new Player();
    Scout scout2 = new Scout();
    Monitoring monitoring2 = new Monitoring();

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        monitoringArrayList = new ArrayList<>();
        // 1 - accepted monitoring
        player1.setId(1L);
        player1.setUsername("player1");
        player1.setName("Player1");
        player1.setSurname("Player1");
        player1.setEmail("player1@footwatch.pl");
        scout1.setId(1L);
        scout1.setUsername("scout1");
        scout1.setName("Scout1");
        scout1.setSurname("Scout1");
        scout1.setEmail("scout1@footwatch.pl");
        monitoring1.setId(new MonitoringPK(1L, 1L));
        monitoring1.setPlayer(player1);
        monitoring1.setScout(scout1);
        monitoring1.setAcceptedByPlayer(true); // key field
        monitoringArrayList.add(monitoring1);

        // 2 - unaccepted monitoring
        player2.setId(2L);
        player2.setUsername("player2");
        player2.setName("Player2");
        player2.setSurname("Player2");
        player2.setEmail("player2@footwatch.pl");
        scout2.setId(2L);
        scout2.setUsername("scout2");
        scout2.setName("Scout2");
        scout2.setSurname("Scout2");
        scout2.setEmail("scout2@footwatch.pl");
        monitoring2.setId(new MonitoringPK(2L, 2L));
        monitoring2.setPlayer(player2);
        monitoring2.setScout(scout2);
        monitoring2.setAcceptedByPlayer(false); // key field
        monitoringArrayList.add(monitoring2);
        Mockito.when(mockMonitoringRepository.findAll()).thenReturn(monitoringArrayList);
        Mockito.when(mockMonitoringRepository.findById(new MonitoringPK(1L, 1L))).
                thenReturn(Optional.of(monitoring1));
        Mockito.when(mockMonitoringRepository.findById(new MonitoringPK(2L, 2L))).
                thenReturn(Optional.of(monitoring2));
    }

    @Test
    void getAllMonitoredPlayers() {
        List<Player> playersList = monitoringServiceWithMocks.getAllMonitoredPlayers("scout1");
        assertThat(playersList).isNotNull();
        assertThat(playersList.get(0).getId()).isEqualTo(1L);
        assertThat(playersList.get(0).getUsername()).isEqualTo("player1");
        assertThat(playersList.get(0).getName()).isEqualTo("Player1");
        assertThat(playersList.get(0).getSurname()).isEqualTo("Player1");
        assertThat(playersList.get(0).getEmail()).isEqualTo("player1@footwatch.pl");
    }

    @Test
    void getAllMonitoringScouts() {
        List<Scout> scoutList = monitoringServiceWithMocks.getAllMonitoringScouts("player1");
        assertThat(scoutList).isNotNull();
        assertThat(scoutList.get(0).getId()).isEqualTo(1L);
        assertThat(scoutList.get(0).getUsername()).isEqualTo("scout1");
        assertThat(scoutList.get(0).getName()).isEqualTo("Scout1");
        assertThat(scoutList.get(0).getSurname()).isEqualTo("Scout1");
        assertThat(scoutList.get(0).getEmail()).isEqualTo("scout1@footwatch.pl");
    }

    @Test
    void getAllScoutsWithPendingRequests() {
        List<Scout> scoutList = monitoringServiceWithMocks.getAllScoutsWithPendingRequests("player2");
        assertThat(scoutList).isNotNull();
        assertThat(scoutList.get(0).getId()).isEqualTo(2L);
        assertThat(scoutList.get(0).getUsername()).isEqualTo("scout2");
        assertThat(scoutList.get(0).getName()).isEqualTo("Scout2");
        assertThat(scoutList.get(0).getSurname()).isEqualTo("Scout2");
        assertThat(scoutList.get(0).getEmail()).isEqualTo("scout2@footwatch.pl");
    }

    @Test
    void getAllPlayersWithPendingRequest() {
        List<Player> playersList = monitoringServiceWithMocks.getAllPlayersWithPendingRequest("scout2");
        assertThat(playersList).isNotNull();
        assertThat(playersList.get(0).getId()).isEqualTo(2L);
        assertThat(playersList.get(0).getUsername()).isEqualTo("player2");
        assertThat(playersList.get(0).getName()).isEqualTo("Player2");
        assertThat(playersList.get(0).getSurname()).isEqualTo("Player2");
        assertThat(playersList.get(0).getEmail()).isEqualTo("player2@footwatch.pl");
    }

    @Test
    void deleteMonitoring() {
        Long scout1Id = monitoring1.getScout().getId();
        Long player1Id = monitoring1.getPlayer().getId();
        monitoringServiceWithMocks.deleteMonitoring(scout1Id, player1Id);
        Mockito.verify(mockMonitoringRepository).delete(monitoring1);
    }

    @Test
    void createNew() {
        Mockito.when(mockMonitoringRepository.save(monitoring2)).thenReturn(monitoring2);
        Monitoring createdMonitoring = monitoringServiceWithMocks.createNew(monitoring2);
        assertThat(createdMonitoring).isSameAs(monitoring2);
    }

    @Test
    void exists() {
        boolean monitoringExists = monitoringServiceWithMocks.exists(monitoring1.getPlayer().getId(),
                monitoring1.getScout().getId());
        assertThat(monitoringExists).isEqualTo(true);
    }

    @Test
    void acceptMonitoring() {
        Mockito.when(mockMonitoringRepository.save(monitoring2)).thenReturn(monitoring2);
        boolean monitoringAccepted = monitoringServiceWithMocks.acceptMonitoring(monitoring2.getScout().getId(),
                monitoring2.getPlayer().getId());
        assertThat(monitoringAccepted).isEqualTo(true);
    }
}