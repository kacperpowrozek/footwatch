package com.footwatch;

import com.footwatch.controller.LoginController;
import com.footwatch.controller.PlayerController;
import com.footwatch.controller.ScoutController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FootwatchApplicationTests {

    @Autowired
    private LoginController loginController;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private ScoutController scoutController;

    @Test
    void checkIfLoginControllerExists() {
        assertThat(loginController).isNotNull();
    }

    @Test
    void checkIfPlayerControllerExists() {
        assertThat(playerController).isNotNull();
    }

    @Test
    void checkIfScoutControllerExists() {
        assertThat(scoutController).isNotNull();
    }
}
