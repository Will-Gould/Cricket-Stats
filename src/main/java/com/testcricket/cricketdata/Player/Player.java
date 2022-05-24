package com.testcricket.cricketdata.Player;

import com.testcricket.cricketdata.People.Person;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
public class Player extends Person {

    private List<String> teams;

    @PersistenceConstructor
    public Player(String identifier, String uniqueName, String name, List<String> teams) {
        super(identifier, uniqueName, name);
        if(teams == null){
            this.teams = new ArrayList<>();
        }else {
            this.teams = teams;
        }
    }

    public Player(String identifier, String uniqueName, String name){
        super(identifier, uniqueName, name);
        this.teams = new ArrayList<>();
    }

    public void addTeam(String team){
        this.teams.add(team);
    }
}
