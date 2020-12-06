package com.footwatch.config;

import com.footwatch.model.Player;
import com.footwatch.model.Scout;
import com.footwatch.service.PlayerService;
import com.footwatch.service.ScoutService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    PlayerService playerService;
    ScoutService scoutService;

    public CustomAuthenticationProvider(PlayerService playerService, ScoutService scoutService) {
        this.playerService = playerService;
        this.scoutService = scoutService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if(playerService.exists(username)) {
            Player player = playerService.findByUsername(username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(password, player.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority("ROLE_PLAYER")));
            }
        }
        else if(scoutService.exists(username)) {
            Scout scout = scoutService.findByUsername(username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(password, scout.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority("ROLE_SCOUT")));
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
