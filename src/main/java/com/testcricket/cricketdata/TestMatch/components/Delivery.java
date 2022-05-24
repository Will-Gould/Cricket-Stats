package com.testcricket.cricketdata.TestMatch.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    private String batter;
    private String bowler;
    private String nonStriker;
    private int batterRuns;
    private int byes;
    private int legByes;
    private int wides;
    private int noBalls;
    private int totalExtras;
    private int totalRuns;
    private List<Wicket> wickets;

    public Delivery(String batter, String bowler, String nonStriker, int batterRuns, int byes, int legByes, int wides, int noBalls, int totalExtras, int totalRuns) {
        this.batter = batter;
        this.bowler = bowler;
        this.nonStriker = nonStriker;
        this.batterRuns = batterRuns;
        this.byes = byes;
        this.legByes = legByes;
        this.wides = wides;
        this.noBalls = noBalls;
        this.totalExtras = totalExtras;
        this.totalRuns = totalRuns;
        wickets = null;
    }

    public Delivery(String batter, String bowler, String nonStriker, int batterRuns, int byes, int legByes, int wides, int noBalls, int totalExtras, int totalRuns, List<Wicket> wickets) {
        this.batter = batter;
        this.bowler = bowler;
        this.nonStriker = nonStriker;
        this.batterRuns = batterRuns;
        this.byes = byes;
        this.legByes = legByes;
        this.wides = wides;
        this.noBalls = noBalls;
        this.totalExtras = totalExtras;
        this.totalRuns = totalRuns;
        this.wickets = wickets;
    }
}
