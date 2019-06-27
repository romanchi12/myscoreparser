package org.romanchi.myscore.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.romanchi.myscore.model.entities.Match;
import org.romanchi.myscore.model.entities.Player;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class MatchDTO {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private Integer id;
    @JsonProperty("home_team")
    private String team1;
    @JsonProperty("host_team")
    private String team2;
    @JsonProperty("home_team_score")
    private Integer score1;
    @JsonProperty("host_team_score")
    private Integer score2;
    private String date;
    private String league;
    @JsonProperty("players")
    private List<PlayerDTO> playersDTO;

    public MatchDTO(Match match){
        this.id = match.getId();
        this.team1 = match.getTeam1();
        this.team2 = match.getTeam2();
        this.score1 = match.getScore1();
        this.score2 = match.getScore2();
        this.date = simpleDateFormat.format(match.getDate());
        this.league = match.getLeague();
        this.playersDTO = match.getPlayers().stream().map(PlayerDTO::new).collect(Collectors.toList());
    }

    public Match toEntity() throws ParseException {
        return Match.builder()
                .id(id)
                .league(league)
                .score1(score1)
                .score2(score2)
                .team1(team1)
                .team2(team2)
                .date(simpleDateFormat.parse(date))
                .build();
    }

}
