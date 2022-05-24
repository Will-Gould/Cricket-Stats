package com.testcricket.cricketdata.Profile;

import com.testcricket.cricketdata.TestMatch.components.Dismissal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BattingInnings {

    private LocalDate date;
    private int score;
    private int ballsFaced;
    private int fours;
    private int sixes;
    private boolean out;
    private Dismissal dismissal;
    private String dismissedByBowler;
    private String dismissedByFielder;


    public BattingInnings(LocalDate date, int score, int ballsFaced, int fours, int sixes, boolean out, Dismissal dismissal, String dismissedByBowler, String dismissedByFielder) {
        this.date = date;
        this.score = score;
        this.ballsFaced = ballsFaced;
        this.fours = fours;
        this.sixes = sixes;
        this.out = out;
        this.dismissal = dismissal;
        this.dismissedByBowler = dismissedByBowler;
        this.dismissedByFielder = dismissedByFielder;
    }

}
