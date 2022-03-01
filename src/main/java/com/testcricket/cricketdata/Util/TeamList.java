package com.testcricket.cricketdata.Util;

import com.testcricket.cricketdata.People.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamList {

    private String name;
    private String id;
    private List<String> players;

    public TeamList(String name, String id) {
        this.name = name;
        this.id = id;
        players = new ArrayList<>();
    }

    public void addPlayer(String player){
        this.players.add(player);
    }
}
