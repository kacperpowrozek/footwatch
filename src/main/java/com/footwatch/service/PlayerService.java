package com.footwatch.service;

import com.footwatch.model.Player;
import com.footwatch.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getOne(Long id) {
        Optional<Player> optional = playerRepository.findById(id);
        return optional.orElse(null);
    }

    public Player createNew(Player newPlayer) {
        return playerRepository.save(newPlayer);
    }

    public boolean exists(String username) {
        List<Player> allPlayers = playerRepository.findAll();
        for (int i = 0; i < allPlayers.size(); i++) {
            Player currentPlayer = allPlayers.get(i);
            if(currentPlayer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Player findByUsername(String username) {
        List<Player> allPlayers = playerRepository.findAll();
        for (int i = 0; i < allPlayers.size(); i++) {
            Player currentPlayer = allPlayers.get(i);
            if (currentPlayer.getUsername().equals(username)) {
                return currentPlayer;
            }
        }
        return null;
    }

    public List<Player> findPlayersByNameAndSurname(String playerName, String playerSurname) {
        if(playerName.length() == 0) {
            return playerRepository.findAll().
                    stream().filter(p -> p.getSurname().equals(playerSurname)).
                    collect(Collectors.toList());
        }
        else {
            return playerRepository.findAll().stream().
                    filter(p -> p.getSurname().equals(playerSurname)).
                    filter(p -> p.getName().equals(playerName)).
                    collect(Collectors.toList());
        }
    }
}