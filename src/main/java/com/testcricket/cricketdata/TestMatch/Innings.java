package com.testcricket.cricketdata.TestMatch;

import com.testcricket.cricketdata.People.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Innings {

    private String team;
    private List<Over> overs;
    private boolean declared;

    public Innings(String team, List<Over> overs, boolean declared) {
        this.team = team;
        this.overs = overs;
        this.declared = declared;
    }
}
