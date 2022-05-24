package com.testcricket.cricketdata.Profile;

import com.testcricket.cricketdata.Player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BowlerProfile extends PlayerProfile{

    private int wickets;
    private int deliveries;
    private int runs;
    private int maidens;
    private double bowlingAverage;
    private int fourWicketInnings;
    private int fiveWicketInnings;
    private int tenWicketMatches;
    private List<BowlingInnings> bowlingInnings;

    public BowlerProfile(Player player, Role roleFocus, List<BattingInnings> battingInnings, List<BowlingInnings> bowlingInnings, int tenWicketMatches) {
        super(player, roleFocus, battingInnings);
        this.bowlingInnings = bowlingInnings;
        this.wickets = findWickets();
        this.deliveries = findDeliveries();
        this.runs = findRuns();
        this.fourWicketInnings = findFourWicketInnings();
        this.fiveWicketInnings = findFiveWicketInnings();
        this.tenWicketMatches = tenWicketMatches;
        this.bowlingAverage = findBowlingAverage();
    }

    private Double findBowlingAverage(){
        double runs = (double) this.runs;
        double wickets = (double) this.wickets;
        return runs/wickets;
    }

    private int findDeliveries(){
        int deliveries = 0;
        for(BowlingInnings i : this.bowlingInnings){
            deliveries += i.getDeliveries();
        }
        return deliveries;
    }

    private int findRuns(){
        int runs = 0;
        for(BowlingInnings i : this.bowlingInnings){
            runs += i.getRuns();
        }
        return runs;
    }

    private int findWickets(){
        int wickets = 0;
        for(BowlingInnings i : this.bowlingInnings){
            wickets += i.getWickets();
        }
        return wickets;
    }

    private int findFourWicketInnings(){
        int fourWickets = 0;
        for(BowlingInnings i : this.bowlingInnings){
            if(i.getWickets() == 4){
                fourWickets++;
            }
        }
        return fourWickets;
    }

    private int findFiveWicketInnings(){
        int fiveWickets = 0;
        for(BowlingInnings i : this.bowlingInnings){
            if(i.getWickets() > 4){
                fiveWickets++;
            }
        }
        return fiveWickets;
    }

}
