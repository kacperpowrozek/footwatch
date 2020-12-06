package com.footwatch.config;

import com.footwatch.service.PlayerService;
import com.footwatch.service.ScoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    PlayerService playerService;
    ScoutService scoutService;

    public SpringSecurityConfig(PlayerService playerService, ScoutService scoutService) {
        this.playerService = playerService;
        this.scoutService = scoutService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register/**", "/css/**").permitAll()
                .antMatchers("/player/**").hasAnyRole("PLAYER")
                .antMatchers("/scout/**").hasAnyRole("SCOUT")
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .successHandler(new UrlAuthenticationSuccessHandler())
                .permitAll()
                .and().logout()
                .permitAll()
                .and();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(new CustomAuthenticationProvider(playerService, scoutService));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}