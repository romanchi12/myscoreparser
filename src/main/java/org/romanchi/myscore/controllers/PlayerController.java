package org.romanchi.myscore.controllers;

import org.romanchi.myscore.model.dtos.MatchDTO;
import org.romanchi.myscore.model.dtos.PlayerDTO;
import org.romanchi.myscore.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping(value = "/all")
    public List<PlayerDTO> all(){
        return playerService.findAll().stream().map(PlayerDTO::new).collect(Collectors.toList());
    }
    @GetMapping(value = "/{player_id}/matches")
    public List<MatchDTO> allMatchesByPlayer(@PathVariable(name = "player_id") Integer playerId){
        return playerService
                .findById(playerId)
                .getMatches()
                .stream()
                .map(MatchDTO::new)
                .collect(Collectors.toList());
    }
}
