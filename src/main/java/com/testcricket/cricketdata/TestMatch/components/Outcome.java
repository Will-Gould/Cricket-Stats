package com.testcricket.cricketdata.TestMatch.components;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Outcome {

    private Result result;
    private String winningTeam;
    private Margin margin;

    public Outcome() {
        result = Result.No_Result;
        margin = null;
    }

    public Outcome(Result result, String winningTeam, Margin margin) {
        this.result = result;
        this.winningTeam = winningTeam;
        this.margin = margin;
    }

    public Outcome(Result result, String winningTeam) {
        this.result = result;
        this.winningTeam = winningTeam;
    }

    public Outcome(Result result) {
        this.result = result;
    }

}
