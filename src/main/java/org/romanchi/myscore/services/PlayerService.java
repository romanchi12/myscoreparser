package org.romanchi.myscore.services;

import org.romanchi.myscore.model.entities.Player;
import org.romanchi.myscore.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll(){
        return playerRepository.findAll();
    }

    public Optional<Player> findById(String id){
        return playerRepository.findById(id);
    }

    public List<Player> saveAll(List<Player> players){
        return playerRepository.saveAll(players);
    }

}
