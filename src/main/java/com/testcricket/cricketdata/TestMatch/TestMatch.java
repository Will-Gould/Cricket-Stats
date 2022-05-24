package com.testcricket.cricketdata.TestMatch;

import com.testcricket.cricketdata.TestMatch.components.Decision;
import com.testcricket.cricketdata.TestMatch.components.Innings;
import com.testcricket.cricketdata.TestMatch.components.Outcome;
import com.testcricket.cricketdata.Util.Gender;
import com.testcricket.cricketdata.Util.MatchType;
import com.testcricket.cricketdata.Util.TeamList;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@Getter
public class TestMatch {

    @Id
    private String id;
    private String fileId;
    private int ballsPerOver;
    private String city;
    private List<LocalDate> dates;
    private String event;
    private int matchNumber;
    private Gender gender;
    private MatchType matchType;
    private int matchTypeNumber;
    private List<String> matchReferees;
    private List<String> tvUmpires;
    private List<String> fieldUmpires;
    private Outcome outcome;
    private List<String> playerOfTheMatch;
    private List<TeamList> teams;
    private String season;
    private String tossWinner;
    private Decision tossDecision;
    private String venue;
    private List<Innings> innings;

    public TestMatch(
            String fileId,
            int ballsPerOver,
            String city,
            List<LocalDate> dates,
            String event,
            int matchNumber,
            Gender gender,
            MatchType matchType,
            int matchTypeNumber,
            List<String> matchReferees,
            List<String> tvUmpires,
            List<String> fieldUmpires,
            Outcome outcome,
            List<String> playerOfTheMatch,
            List<TeamList> teams,
            String season,
            String tossWinner,
            Decision tossDecision,
            String venue,
            List<Innings> innings) {

        this.fileId = fileId;
        this.ballsPerOver = ballsPerOver;
        this.city = city;
        this.dates = dates;
        this.event = event;
        this.matchNumber = matchNumber;
        this.gender = gender;
        this.matchType = matchType;
        this.matchTypeNumber = matchTypeNumber;
        this.matchReferees = matchReferees;
        this.tvUmpires = tvUmpires;
        this.fieldUmpires = fieldUmpires;
        this.outcome = outcome;
        this.playerOfTheMatch = playerOfTheMatch;
        this.teams = teams;
        this.season = season;
        this.tossWinner = tossWinner;
        this.tossDecision = tossDecision;
        this.venue = venue;
        this.innings = innings;

    }

    public TestMatch(
            String id,
            String fileId,
            int ballsPerOver,
            String city,
            List<LocalDate> dates,
            String event,
            int matchNumber,
            Gender gender,
            MatchType matchType,
            int matchTypeNumber,
            List<String> matchReferees,
            List<String> tvUmpires,
            List<String> fieldUmpires,
            Outcome outcome,
            List<String> playerOfTheMatch,
            List<TeamList> teams,
            String season,
            String tossWinner,
            Decision tossDecision,
            String venue,
            List<Innings> innings) {

        this.id = id;
        this.fileId = fileId;
        this.ballsPerOver = ballsPerOver;
        this.city = city;
        this.dates = dates;
        this.event = event;
        this.matchNumber = matchNumber;
        this.gender = gender;
        this.matchType = matchType;
        this.matchTypeNumber = matchTypeNumber;
        this.matchReferees = matchReferees;
        this.tvUmpires = tvUmpires;
        this.fieldUmpires = fieldUmpires;
        this.outcome = outcome;
        this.playerOfTheMatch = playerOfTheMatch;
        this.teams = teams;
        this.season = season;
        this.tossWinner = tossWinner;
        this.tossDecision = tossDecision;
        this.venue = venue;
        this.innings = innings;

    }

    public String getId(){
        return this.id;
    }

    public List<String> getAllPlayers(){
        ArrayList<String> players = new ArrayList<>();
        for(TeamList t : this.teams){
            players.addAll(t.getPlayers());
        }
        return players;
    }

}
