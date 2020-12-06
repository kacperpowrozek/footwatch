package com.footwatch.service;

import com.footwatch.model.Scout;
import com.footwatch.repository.ScoutRepository;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScoutServiceTest {

    @InjectMocks
    private ScoutService scoutServiceWithMocks;

    @Mock
    private ScoutRepository mockScoutRepository;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        ArrayList<Scout> scoutArrayList = new ArrayList<>();
        Scout scout = new Scout();
        scout.setId(1L);
        scout.setUsername("scout1");
        scout.setName("Scout1");
        scout.setSurname("Scout1");
        scout.setEmail("scout1@footwatch.pl");
        scoutArrayList.add(scout);
        Mockito.when(mockScoutRepository.findAll()).thenReturn(scoutArrayList);
        Mockito.when(mockScoutRepository.findById(1L)).thenReturn(Optional.of(scout));
    }

    @Test
    void getOne() {
        Scout scout = scoutServiceWithMocks.getOne(1L);
        assertThat(scout).isNotNull();
        assertThat(scout.getId()).isEqualTo(1L);
        assertThat(scout.getUsername()).isEqualTo("scout1");
    }

    @Test
    void createNew() {
        Scout scout = new Scout();
        scout.setEmail("test@footwatch.pl");
        scout.setName("Foo");
        scout.setSurname("Surname");
        Mockito.when(mockScoutRepository.save(scout)).thenReturn(scout);
        Scout createdScout = scoutServiceWithMocks.createNew(scout);
        assertThat(createdScout).isSameAs(scout);
    }

    @Test
    void exists() {
        boolean scoutExists = scoutServiceWithMocks.exists("scout1");
        assertThat(scoutExists).isEqualTo(true);
    }

    @Test
    void findByUsername() {
        Scout scout = scoutServiceWithMocks.findByUsername("scout1");
        assertThat(scout).isNotNull();
        assertThat(scout.getEmail()).isEqualTo("scout1@footwatch.pl");
    }
}