package org.romanchi.myscore.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Players")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthdate")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "wins")
    private Integer wins;
    @Column(name = "all_games")
    private Integer allGames;
    @Column(name = "draws")
    private Integer draws;
    @Column(name = "injury_amount")
    private Integer injuryAmount;
    @Column(name = "team_name")
    private String teamName;

    @ManyToMany(mappedBy = "players")
    private List<Match> matches;

}
