package org.romanchi.myscore.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.romanchi.myscore.model.entities.Player;

import javax.persistence.Column;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@Builder
public class PlayerDTO {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private String id;
    private String name;
    private String birthdate;
    private Integer matchesAmount;
    private Integer wins;
    private Integer draws;
    private Integer injuryAmount;
    private String teamName;
    private Integer allGames;

    public PlayerDTO(Player player){
        this.id = player.getId();
        this.name = player.getName();
        this.birthdate = player.getBirthday()==null
                ? null
                : simpleDateFormat.format(player.getBirthday());
        this.matchesAmount = player.getMatches() == null ? 0 : player.getMatches().size();
    }

    public Player toEntity() throws ParseException {
        return Player.builder()
                .id(id)
                .name(name)
                .birthday(birthdate == null ? null : simpleDateFormat.parse(birthdate))
                .wins(wins)
                .injuryAmount(injuryAmount)
                .draws(draws)
                .teamName(teamName)
                .allGames(allGames)
                .build();
    }
}
