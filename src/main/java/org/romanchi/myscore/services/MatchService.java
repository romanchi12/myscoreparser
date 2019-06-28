package org.romanchi.myscore.services;

import lombok.AllArgsConstructor;
import org.romanchi.myscore.model.entities.Match;
import org.romanchi.myscore.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> findAll(PageRequest pageRequest){
        return matchRepository.findAll(pageRequest).getContent();
    }

    public void save(Match match) {
        matchRepository.save(match);
    }
}
