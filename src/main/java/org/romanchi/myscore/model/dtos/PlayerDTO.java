package org.romanchi.myscore.model.dtos;

import lombok.Data;
import org.romanchi.myscore.model.entities.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
public class PlayerDTO {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private Integer id;
    private String name;
    private String birthdate;
    private Integer matchesAmount;

    public PlayerDTO(Player player){
        this.id = player.getId();
        this.name = player.getName();
        this.birthdate = simpleDateFormat.format(player.getBirthday());
        this.matchesAmount = player.getMatches().size();
    }

    public Player toEntity() throws ParseException {
        return Player.builder()
                .id(id)
                .name(name)
                .birthday(simpleDateFormat.parse(birthdate))
                .build();
    }
}
