package com.testcricket.cricketdata.Profile;

import com.testcricket.cricketdata.People.Player;
import com.testcricket.cricketdata.People.PlayerRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayerProfile {

    private Player player;
    private Role roleFocus;
    private int totalRuns;
    private int highestScore;
    private double average;
    private int fifties;
    private int centuries;
    private List<BattingInnings> battingInnings;

    public PlayerProfile(Player player, Role roleFocus){
        this.player = player;
        this.roleFocus = roleFocus;
        this.battingInnings = new ArrayList<>();
    }

    public PlayerProfile(Player player, Role roleFocus, List<BattingInnings> battingInnings) {
        this.player = player;
        this.roleFocus = roleFocus;
        this.battingInnings = battingInnings;
        this.average = findBattingAverage();
        this.totalRuns = findTotalRuns();
        this.highestScore = findHighestScore();
        this.fifties = findFifties();
        this.centuries = findCenturies();
    }

    private int findTotalRuns(){
        int totalRuns = 0;
        for(BattingInnings i : this.battingInnings){
            totalRuns += i.getScore();
        }
        return totalRuns;
    }

    private int findHighestScore(){
        int highestScore = 0;
        for(BattingInnings i : this.battingInnings){
            if(i.getScore() > highestScore){
                highestScore = i.getScore();
            }
        }
        return highestScore;
    }

    private double findBattingAverage(){
        double totalRuns = 0;
        double dismissals = 0;
        for(BattingInnings i : this.battingInnings){
            totalRuns += i.getScore();
            if(i.getDismissal() != null){
                dismissals++;
            }
        }
        return totalRuns/dismissals;
    }

    private int findFifties(){
        int fifties = 0;
        for(BattingInnings i : this.battingInnings){
            if(i.getScore() > 49 && i.getScore() < 100){
                fifties++;
            }
        }
        return fifties;
    }

    private int findCenturies(){
        int centuries = 0;
        for(BattingInnings i : this.battingInnings){
            if(i.getScore() > 99){
                centuries++;
            }
        }
        return centuries;
    }

    public void addBattingInnings(BattingInnings battingInnings){
        this.battingInnings.add(battingInnings);
    }

}
