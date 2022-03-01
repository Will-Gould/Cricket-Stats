package com.testcricket.cricketdata.Profile;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class VersusBowlerStatistics {

    private HashMap<String, Double> bestStrikeRateBowlers;
    private HashMap<String, Double> worstStrikeRateBowlers;

    public VersusBowlerStatistics(HashMap<String, Double> bestStrikeRateBowlers, HashMap<String, Double> worstStrikeRateBowlers){
        this.bestStrikeRateBowlers = bestStrikeRateBowlers;
        this.worstStrikeRateBowlers = worstStrikeRateBowlers;
    }

}
