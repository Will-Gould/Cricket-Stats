package com.testcricket.cricketdata.Player;

import com.testcricket.cricketdata.Profile.*;
import com.testcricket.cricketdata.Series.Series;
import com.testcricket.cricketdata.Series.SeriesRepository;
import com.testcricket.cricketdata.TestMatch.*;
import com.testcricket.cricketdata.TestMatch.TestMatchRepository;
import com.testcricket.cricketdata.TestMatch.components.*;
import com.testcricket.cricketdata.Util.StrikeRate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TestMatchRepository testMatchRepository;
    private final SeriesRepository seriesRepository;


    public List<Player> fetchAllPlayers(){
        return playerRepository.findAll();
    }

    public Optional<PlayerProfile> fetchPlayerProfile(String id, Role role){
        Optional<Player> p = playerRepository.findById(id);
        if(p.isEmpty()){
            System.out.println("P is empty!");
            return Optional.empty();
        }
        Player player = p.get();

//        switch(profileType){
//            case "batter" -> roleFocus = Role.BATTER;
//            case "bowler" -> roleFocus = Role.BOWLER;
//            case "all rounder" -> roleFocus = Role.ALL_ROUNDER;
//            case "wicket keeper" -> roleFocus = Role.WICKET_KEEPER;
//        }

        //Get list of matches featuring this player
        List<TestMatch> testMatches = testMatchRepository.fetchFeaturedTestMatches(player.getId().toString());

        switch(role){
            case BATTER -> {
                return Optional.of(getBatterProfile(player, testMatches));
            }
            case BOWLER -> {
                return Optional.of(getBowlerProfile(player, testMatches));
            }
        }

        return Optional.of(getBatterProfile(player, testMatches));

    }

    public Optional<PlayerProfile> fetchPlayerProfile(String id, Role role, String series){
        Optional<Player> p = playerRepository.findById(id);
        if(p.isEmpty()){
            System.out.println("P is empty!");
            return Optional.empty();
        }
        Player player = p.get();

//        switch(profileType){
//            case "batter" -> roleFocus = Role.BATTER;
//            case "bowler" -> roleFocus = Role.BOWLER;
//            case "all rounder" -> roleFocus = Role.ALL_ROUNDER;
//            case "wicket keeper" -> roleFocus = Role.WICKET_KEEPER;
//        }

        //Get list of matches from this series
        Optional<Series> o = seriesRepository.findById(series);
        ArrayList<TestMatch> testMatches = new ArrayList<>();

        Series s;
        if(o.isEmpty()){
            return Optional.empty();
        }else{
            s = o.get();
        }

        s.getMatches().forEach(m -> {
            Optional<TestMatch> t = testMatchRepository.findById(m);
            t.ifPresent(testMatches::add);
        });

        switch(role){
            case BATTER -> {
                return Optional.of(getBatterProfile(player, testMatches));
            }
            case BOWLER -> {
                return Optional.of(getBowlerProfile(player, testMatches));
            }
        }

        return Optional.of(getBatterProfile(player, testMatches));

    }
//
//    public Optional<PlayerProfile> fetchPlayerProfile(String id, Role role, String season){
//
//    }

    private PlayerProfile getBatterProfile(Player p, List<TestMatch> testMatches){

        //Get list of innings featuring this player
        List<Innings> featuredInnings = getFeaturedInnings(p, testMatches);

        //Get batting innings
        List<BattingInnings> battedInnings = getBattingInnings(p, testMatches);

        //Get dismissal statistics
        DismissalStatistics dismissalStatistics = getDismissalStatistics(battedInnings);

        //Get versus bowler statistics
        VersusBowlerStatistics versusBowlerStatistics = getVersusBowlerStatistics(p, featuredInnings);

        return new BatterProfile(p, Role.BATTER, battedInnings, versusBowlerStatistics, dismissalStatistics);
    }

    private PlayerProfile getBowlerProfile(Player p, List<TestMatch> testMatches){

        //Get batted innings
        List<BattingInnings> battingInnings = getBattingInnings(p, testMatches);

        //Get Bowling innings
        List<BowlingInnings> bowlingInnings = getBowlingInnings(p, testMatches);

        //Get ten wickets matches
        int tenWicketMatches = getTenWicketMatches(p, testMatches);

        return new BowlerProfile(p, Role.BOWLER, battingInnings, bowlingInnings,tenWicketMatches);
    }

    private List<BattingInnings> getBattingInnings(Player player, List<TestMatch> testMatches){
        //Get all the innings which player batted in
        List<BattingInnings> battedInnings = new ArrayList<>();
        for(TestMatch t : testMatches){
            int inningsCount = 0;
            for(Innings i : t.getInnings()){
                inningsCount++;
                LocalDate date;
                if(t.getDates().size() < inningsCount){
                    date = t.getDates().get(t.getDates().size()-1);
                }else{
                    date = t.getDates().get(inningsCount - 1);
                }
                boolean batted = false;
                int runs = 0;
                int ballsFaced = 0;
                int fours = 0;
                int sixes = 0;
                boolean out = false;
                Dismissal dismissal = null;
                String dismissedByBowler = null;
                String dismissedByFielder = null;
                for(Over o : i.getOvers()){
                    for(Delivery d : o.getDeliveries()){
                        if((d.getBatter().equals(player.getId()) || d.getNonStriker().equals(player.getId())) && !batted){
                            batted = true;
                        }
                        if(d.getBatter().equals(player.getId())){
                            runs += d.getBatterRuns();
                            if(d.getBatterRuns() == 4){
                                fours++;
                            }
                            if(d.getBatterRuns() == 6){
                                sixes++;
                            }
                            ballsFaced++;
                        }
                        if(d.getWickets() != null){
                            for(Wicket w : d.getWickets()){
                                if(w.getOutgoingBatter().equals(player.getId())){
                                    out = true;
                                    dismissal = w.getType();

                                    //Debug prints
//                                    System.out.println("Dismissal:");
//                                    System.out.println("  - Type:       " + dismissal);
//                                    System.out.println("  - Bowler:     " + d.getBowler());
//                                    System.out.println("  - Match ID:   " + t.getFileId());
//                                    System.out.println("  - Over No:    " + o.getOverNumber());

                                    switch (w.getType()){
                                        //TODO Data source has not recorded fielders involved in run outs
//                                        case Run_Out -> {
//                                            if(w.getFielders().size() > 0){
//                                                dismissedByFielder = w.getFielders().get(0);
//                                            }
//                                        }
                                        case Stumped, Caught -> {
                                            dismissedByBowler = d.getBowler();
                                            dismissedByFielder = w.getFielders().get(0);
                                        }
                                        case Bowled, LBW, Hit_Wicket -> dismissedByBowler = d.getBowler();
                                    }
                                }
                            }
                        }
                    }
                }
                if(batted){
                    BattingInnings batterInnings = new BattingInnings(date, runs, ballsFaced, fours, sixes, out, dismissal, dismissedByBowler, dismissedByFielder);
                    battedInnings.add(batterInnings);
                }
            }
        }
        return battedInnings;
    }

    private List<BowlingInnings> getBowlingInnings(Player player, List<TestMatch> testMatches){
        List<BowlingInnings> bowlingInnings = new ArrayList<>();
        for(TestMatch t : testMatches){
            int inningsCount = 0;
            for(Innings i : t.getInnings()){
                List<String> playersDismissed = new ArrayList<>();
                inningsCount++;
                LocalDate date;
                if(t.getDates().size() < inningsCount){
                    date = t.getDates().get(t.getDates().size()-1);
                }else{
                    date = t.getDates().get(inningsCount - 1);
                }
                boolean bowled = false;
                int overs = 0;
                int maidens = 0;
                int deliveries = 0;
                int dotBalls = 0;
                int runs = 0;
                int wickets = 0;
                int wides = 0;
                int noBalls = 0;
                for(Over o : i.getOvers()){
                    int bowlerCountedRuns = 0;
                    if(o.getDeliveries().get(0).getBowler().equals(player.getId())){
                        bowled = true;
                        overs++;
                    }
                    for(Delivery d: o.getDeliveries()){
                        if(d.getBowler().equals(player.getId())){
                            deliveries++;
                            bowlerCountedRuns += d.getBatterRuns() + d.getWides() + d.getNoBalls();
                            runs += d.getBatterRuns() + d.getWides() + d.getNoBalls();
                            wides += d.getWides();
                            noBalls += d.getNoBalls();
                            if(d.getBatterRuns() + d.getNoBalls() + d.getWides() == 0){
                                dotBalls++;
                            }
                            if(d.getWickets() != null){
                                for(Wicket w : d.getWickets()){
                                    if(w.getType().equals(Dismissal.Bowled) || w.getType().equals(Dismissal.Caught) || w.getType().equals(Dismissal.LBW) || w.getType().equals(Dismissal.Hit_Wicket) || w.getType().equals(Dismissal.Stumped)){
                                        wickets++;
                                        playersDismissed.add(w.getOutgoingBatter());
                                    }
                                }
                            }
                        }
                    }
                    if(o.getDeliveries().get(0).getBowler().equals(player.getId()) && bowlerCountedRuns == 0){
                        maidens++;
                    }
                }
                if(bowled){
                   bowlingInnings.add(new BowlingInnings(date, overs, maidens, deliveries, dotBalls, runs, wickets, wides, noBalls, playersDismissed));
                }
            }
        }
        return bowlingInnings;
    }

    private DismissalStatistics getDismissalStatistics(List<BattingInnings> innings){
        HashMap<Dismissal, Integer> dismissalCount = new HashMap<>();
        HashMap<String, Integer> mostDismissedBy = new HashMap<>();

        Map<String, Integer> dismissedBy = new HashMap<>();
        for(BattingInnings i : innings){
            if(!i.isOut()){
                continue;
            }
            if(i.getDismissal() == null){
                continue;
            }

            //Add dismissal to dismissal count check if dismissal mode exists in hash map first
            if(!dismissalCount.containsKey(i.getDismissal())){
                dismissalCount.put(i.getDismissal(), 1);
            }else{
                int currentCount = dismissalCount.get(i.getDismissal());
                currentCount++;
                dismissalCount.put(i.getDismissal(), currentCount);
            }

            //Get bowler credited with dismissal
            String bowlerId = i.getDismissedByBowler();
            switch(i.getDismissal()){
                case Bowled, Caught, LBW, Stumped, Hit_Wicket -> {
                    if(!dismissedBy.containsKey(bowlerId)){
                        dismissedBy.put(bowlerId, 1);
                    }else{
                        int currentCount = dismissedBy.get(bowlerId);
                        currentCount++;
                        dismissedBy.put(bowlerId, currentCount);
                    }
                }
            }
        }

        int count = 0;
        while(count < 5 && dismissedBy.size() > 0){
            String highestId = null;
            for(String e : dismissedBy.keySet()){
                if(highestId == null){
                    highestId = e;
                    continue;
                }
                if(dismissedBy.get(e) > dismissedBy.get(highestId)){
                    highestId = e;
                }
            }
            if(highestId != null){
                Optional<Player> temp = playerRepository.findById(highestId);
                if(temp.isPresent()){
                    Player p = temp.get();
                    mostDismissedBy.put(p.getUniqueName(), dismissedBy.get(highestId));
                }
                dismissedBy.remove(highestId);
            }
            count++;
        }
        return new DismissalStatistics(dismissalCount, mostDismissedBy);
    }

    private VersusBowlerStatistics getVersusBowlerStatistics(Player player, List<Innings> innings){
        HashMap<String, Double> bestBowlerStrikeRate = new HashMap<>();
        HashMap<String, Double> worstBowlerStrikeRate = new HashMap<>();
        final int minimumBallsFaced = 60;

        HashMap<String, StrikeRate> bowlers = new HashMap<>();
        for(Innings i : innings){
            for(Over o : i.getOvers()){
                for(Delivery d : o.getDeliveries()){
                    if(!d.getBatter().equals(player.getId())){
                        continue;
                    }
                    if(!bowlers.containsKey(d.getBowler())){
                        bowlers.put(d.getBowler(), new StrikeRate());
                    }
                    bowlers.get(d.getBowler()).addRuns(d.getBatterRuns());
                    bowlers.get(d.getBowler()).incrementBallsFaced();
                }
            }
        }

        //Find best and worst strike rates for bowlers for whom the batter has faced 36 or more balls
        int count = 0;
        while(count < 5 && bowlers.size() > 0){
            String highestId = null;
            String lowestId = null;
            for(String bowler : bowlers.keySet()){
                if(highestId == null){
                    highestId = bowler;
                }
                if(lowestId == null){
                    lowestId = bowler;
                }
                if((bowlers.get(bowler).getStrikeRate() > bowlers.get(highestId).getStrikeRate()) && bowlers.get(bowler).getBallsFaced() >= minimumBallsFaced){
                    highestId = bowler;
                }
                if((bowlers.get(bowler).getStrikeRate() < bowlers.get(lowestId).getStrikeRate()) &&bowlers.get(bowler).getBallsFaced() >= minimumBallsFaced){
                    lowestId = bowler;
                }
            }
            bestBowlerStrikeRate.put(highestId, bowlers.get(highestId).getStrikeRate());
            worstBowlerStrikeRate.put(lowestId, bowlers.get(lowestId).getStrikeRate());
            bowlers.remove(highestId);
            if(!bowlers.isEmpty()){
                bowlers.remove(lowestId);
            }
            count++;
        }
        return new VersusBowlerStatistics(bestBowlerStrikeRate, worstBowlerStrikeRate);
    }

    private List<Innings> getFeaturedInnings(Player player, List<TestMatch> testMatches){
        List<Innings> innings = new ArrayList<>();
        for(TestMatch t : testMatches){
            for(Innings i : t.getInnings()){
                boolean batted = false;
                for(Over o : i.getOvers()){
                    if(batted){
                        break;
                    }
                    for(Delivery d : o.getDeliveries()){
                        if(d.getBatter().equals(player.getId())){
                            batted = true;
                            break;
                        }
                    }
                }
                if(batted){
                    innings.add(i);
                }
            }
        }
        return innings;
    }

    private int getTenWicketMatches(Player p, List<TestMatch> testMatches){
        int tenWicketHauls = 0;
        for(TestMatch t : testMatches){
            int matchWickets = 0;
            for(Innings i : t.getInnings()){
                for(Over o : i.getOvers()){
                    for(Delivery d : o.getDeliveries()){
                        if(d.getBowler().equals(p.getId())){
                            if(d.getWickets() != null){
                                for(Wicket w : d.getWickets()){
                                    if(w.getType().equals(Dismissal.Bowled) || w.getType().equals(Dismissal.Caught) || w.getType().equals(Dismissal.LBW) || w.getType().equals(Dismissal.Hit_Wicket) || w.getType().equals(Dismissal.Stumped)){
                                        matchWickets++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(matchWickets > 9){
                tenWicketHauls++;
            }
        }
        return tenWicketHauls;
    }

    private boolean didPlayerFeature(Player p, TestMatch t){
        List<String> players = t.getAllPlayers();
        for(String s : players){
            if(s.equals(p.getId())){
                return true;
            }
        }
        return false;
    }

}
