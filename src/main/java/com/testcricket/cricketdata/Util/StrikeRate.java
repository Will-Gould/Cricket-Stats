package com.testcricket.cricketdata.Util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrikeRate {

    private double ballsFaced;
    private double runs;

    public StrikeRate(){
        this.ballsFaced = 0;
        this.runs = 0;
    }

    public double getStrikeRate(){
        return (this.runs/this.ballsFaced)*100;
    }

    public void addRuns(int runs){
        this.runs += runs;
    }

    public void incrementBallsFaced(){
        this.ballsFaced++;
    }

}
