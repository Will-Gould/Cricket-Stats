package com.testcricket.cricketdata.Profile;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BowlingInnings {

    private LocalDate date;
    private int overs;
    private int maidens;
    private int deliveries;
    private int dotBalls;
    private int runs;
    private int wickets;
    private int wides;
    private int noBalls;
    private List<String> playersDismissed;

    public BowlingInnings(LocalDate date, int overs, int maidens, int deliveries, int dotBalls, int runs, int wickets, int wides, int noBalls, List<String> playersDismissed) {
        this.date = date;
        this.overs = overs;
        this.maidens = maidens;
        this.deliveries = deliveries;
        this.dotBalls = dotBalls;
        this.runs = runs;
        this.wickets = wickets;
        this.wides = wides;
        this.noBalls = noBalls;
        this.playersDismissed = playersDismissed;
    }
}
