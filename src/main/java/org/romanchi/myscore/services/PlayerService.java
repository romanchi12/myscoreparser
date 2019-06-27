package org.romanchi.myscore.services;

import org.romanchi.myscore.model.entities.Player;
import org.romanchi.myscore.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Player findById(Integer id){
        return playerRepository.findById(id).orElseThrow(NullPointerException::new);
    }
}
