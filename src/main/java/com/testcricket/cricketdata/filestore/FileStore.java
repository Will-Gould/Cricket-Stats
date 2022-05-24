package com.testcricket.cricketdata.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testcricket.cricketdata.People.Official;
import com.testcricket.cricketdata.People.OfficialRepository;
import com.testcricket.cricketdata.People.Person;
import com.testcricket.cricketdata.Player.Player;
import com.testcricket.cricketdata.Player.PlayerRepository;
import com.testcricket.cricketdata.Series.CustomSeriesRepositoryImpl;
import com.testcricket.cricketdata.Series.Series;
import com.testcricket.cricketdata.Series.SeriesRepository;
import com.testcricket.cricketdata.Team.Team;
import com.testcricket.cricketdata.Team.TeamRepository;
import com.testcricket.cricketdata.TestMatch.TestMatch;
import com.testcricket.cricketdata.TestMatch.TestMatchRepository;
import com.testcricket.cricketdata.TestMatch.components.*;
import com.testcricket.cricketdata.Util.Gender;
import com.testcricket.cricketdata.Util.MatchType;
import com.testcricket.cricketdata.Util.TeamList;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static com.testcricket.cricketdata.bucket.BucketName.TEST_MATCHES;

@AllArgsConstructor
@Service
public class FileStore {

    private final AmazonS3 s3;
    private final PlayerRepository playerRepository;
    private final TestMatchRepository matchRepository;
    private final OfficialRepository officialRepository;
    private final TeamRepository teamRepository;
    private final SeriesRepository seriesRepository;
    private final CustomSeriesRepositoryImpl customSeriesRepository;

    public void save(
            String path,
            String fileName,
            Optional<Map<String, String>> optionalMetadata,
            InputStream inputStream){

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()){
                map.forEach(metadata::addUserMetadata);
            }
        });
        try{
            s3.putObject(path, fileName, inputStream, metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3 ", e);
        }
    }

    public void delete(String fileName){
        try{
            s3.deleteObject(TEST_MATCHES.getBucketName(), fileName);
        }catch (Exception e){
            throw new IllegalStateException("Failed to delete object ", e);
        }
    }

    public List<String> listFiles(){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(TEST_MATCHES.getBucketName());

        List<String> keys = new ArrayList<>();

        ObjectListing objects = s3.listObjects(listObjectsRequest);

        List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();

        if(objectSummaries.size() < 1){
             return keys;
        }

        for(S3ObjectSummary i : objectSummaries){
            if(!i.getKey().endsWith("/")){
                keys.add(i.getKey());
            }
        }

        return keys;
    }

    public void addNewTestMatches(List<String> newTests) {

        for(String s : newTests){
            Gson gson = new GsonBuilder().create();
            GetObjectRequest getObjectRequest = new GetObjectRequest(TEST_MATCHES.getBucketName(), s);
            S3Object object = s3.getObject(getObjectRequest);

            String[] tokens = s.split("[.]");
            String fileId = tokens[0];

            try(S3ObjectInputStream s3is = object.getObjectContent()){
                org.apache.tomcat.util.json.JSONParser jsonParser = new org.apache.tomcat.util.json.JSONParser(s3is);
                String jsonString = new Gson().toJson(jsonParser.parse(), Map.class);
                JSONObject matchObject = new JSONObject(jsonString);

                loadTestMatchToDatabase(fileId, matchObject);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    //Good lord
    private void loadTestMatchToDatabase(String fileId, JSONObject matchObject){
        //Variables needed for test match object

        System.out.println(fileId);
        int ballsPerOver;
        String city = null;
        List<LocalDate> dates;
        String event = null;
        int matchNumber;
        Gender gender;
        MatchType matchType;
        int matchTypeNumber;
        List<String> matchReferees;
        List<String> tvUmpires;
        List<String> fieldUmpires;
        Outcome outcome;
        List<String> playerOfTheMatch;
        List<TeamList> teamLists;
        String season;
        String tossWinner;
        Decision tossDecision;
        String venue;
        List<Innings> innings;

        //Series values
        String seriesName;

        //Utility variables
        List<Official> officials = new ArrayList<>();

        //FileInputStream is = new FileInputStream(directoryListing[0]);
        JSONParser parser = new JSONParser();
//        FileReader fr = null;
//        try {
//            fr = new FileReader(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        //FileReader fr = new FileReader(new File("C://Users/willg/Desktop/Cricket Stats/cleaned/225264.json"));
//        //FileReader fr = new FileReader(new File("C://Users/willg/Desktop/Cricket Stats/cleaned/64111.json"));
//        //FileReader fr = new FileReader(new File("C://Users/willg/Desktop/Cricket Stats/cleaned/225265.json"));
//        //System.out.println("File Name: " + directoryListing[0].getName() + "\n");
//
//        Object obj = null;
//        try {
//            obj = parser.parse(fr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject(obj.toString());
        JSONObject info = (JSONObject) matchObject.get("info");
        JSONObject registry = (JSONObject) info.get("registry");
        JSONObject jsonPeople = (JSONObject) registry.get("people");

        ArrayList<Person> people = new ArrayList<>();
        ArrayList<Person> tempPeople = new ArrayList<>();
        jsonPeople.keySet().forEach(key -> {
            Person p = new Person(jsonPeople.get(key).toString(), key.toString(), key.toString());
            people.add(p);
        });

        //Get teams and insert if new teams
        //Create team lists
        //Use team names in ALL CAPS
        teamLists = new ArrayList<>();
        JSONObject jsonPlayers = (JSONObject) info.get("players");
        for (String name:jsonPlayers.keySet()) {
            String nameFormat = name.toUpperCase();
            if(!teamRepository.existsByName(nameFormat)){
                Team team = new Team(nameFormat);
                teamRepository.insert(team);
            }
            TeamList teamList = new TeamList(nameFormat, teamRepository.findByName(nameFormat).getId());

            //Sort players into teams
            //Check if they exist in player repository
            //insert new players
            JSONArray jsonNames = (JSONArray) jsonPlayers.get(name);
            for(Object identifier : jsonNames){
                Person person = findPerson(identifier.toString(), people);
                ArrayList<String> pt = new ArrayList<>();
                //Get team ID from repository
                pt.add(teamRepository.findByName(nameFormat).getId());
                Player p = new Player(person.getIdentifier(), person.getUniqueName(), person.getName(), pt);

                //Check if players exists and has played for this team before
                if(playerRepository.findPlayerByIdentifier(p.getIdentifier()) == null){
                    playerRepository.insert(p);
                }else{
                    //If player has not played for this team before insert new team
                    //TODO remove magic number??
                    //size of pt should always be 1
                    if(playerRepository.findPlayerByIdentifier(p.getIdentifier()).getTeams() == null){
                        Player tempPlayer = playerRepository.findPlayerByIdentifier(p.getIdentifier());
                        tempPlayer.addTeam(pt.get(0));
                        playerRepository.save(tempPlayer);
                    }else{
                        if(!playerRepository.findPlayerByIdentifier(p.getIdentifier()).getTeams().contains(pt.get(0))){
                            Player tempPlayer = playerRepository.findPlayerByIdentifier(p.getIdentifier());
                            tempPlayer.addTeam(pt.get(0));
                            playerRepository.save(tempPlayer);
                        }
                    }
                }
                //Get full player objects from mongodb to fill out team lists
                p = playerRepository.findPlayerByIdentifier(p.getIdentifier());
                tempPeople.add(p);
                teamList.addPlayer(p.getId().toString());

                //remove from people list
                people.remove(findPersonByIdentifier(p.getIdentifier(), people));
            }
            teamLists.add(teamList);
        }

        //Get match officials
        //Insert officials into repository
        matchReferees = new ArrayList<>();
        JSONObject jsonOfficials = (JSONObject) info.get("officials");
        JSONArray matchRef = (JSONArray) jsonOfficials.get("match_referees");
        matchRef.forEach(ref -> {
            //Find referee in list of people
            Person p;
            if((p = findPerson(ref.toString(), people)) != null){
                Official o = new Official(p.getIdentifier(), p.getUniqueName(), p.getName());
                officials.add(o);
                //Check if official exists in repository add if not
                if(officialRepository.findByIdentifier(o.getIdentifier()) == null){
                    officialRepository.insert(o);
                }
                o = officialRepository.findByIdentifier(o.getIdentifier());
                tempPeople.add(o);
                matchReferees.add(o.getId().toString());
                people.remove(findPersonByIdentifier(o.getIdentifier(), people));
            }
        });

        //Get tv umpires
        tvUmpires = new ArrayList<>();
        if(keysContain(jsonOfficials.keySet(), "tv_umpires")){
            JSONArray tvUmps = (JSONArray) jsonOfficials.get("tv_umpires");
            tvUmps.forEach(ref -> {
                Person p;
                if((p = findPerson(ref.toString(), people)) != null){
                    Official o = new Official(p.getIdentifier(), p.getUniqueName(), p.getName());
                    officials.add(o);
                    //Check if official exists in repository add if not
                    if(officialRepository.findByIdentifier(o.getIdentifier()) == null){
                        officialRepository.insert(o);
                    }
                    o = officialRepository.findByIdentifier(o.getIdentifier());
                    tempPeople.add(o);
                    tvUmpires.add(o.getId().toString());
                    people.remove(findPersonByIdentifier(o.getIdentifier(), people));
                }
            });
        }

        //Get field umpires
        fieldUmpires = new ArrayList<>();
        JSONArray fUmps = (JSONArray) jsonOfficials.get("umpires");
        fUmps.forEach(ref -> {
            Person p;
            if((p = findPerson(ref.toString(), people)) != null){
                Official o = new Official(p.getIdentifier(), p.getUniqueName(), p.getName());
                officials.add(o);
                //Check if official exists in repository add if not
                if(officialRepository.findByIdentifier(o.getIdentifier()) == null){
                    officialRepository.insert(o);
                }
                o = officialRepository.findByIdentifier(o.getIdentifier());
                tempPeople.add(o);
                fieldUmpires.add(o.getId().toString());
                people.remove(findPersonByIdentifier(o.getIdentifier(), people));
            }
        });

        //Catch any substitutes that may have fallen through the cracks
        //Only remaining people in people list should be substitutes
        for(Person p : people){
            Player player = new Player(p.getIdentifier(), p.getUniqueName(), p.getName(), null);
            if(playerRepository.findPlayerByIdentifier(p.getIdentifier()) == null){
                playerRepository.save(player);
            }
            player = playerRepository.findPlayerByIdentifier(p.getIdentifier());
            tempPeople.add(player);
        }

        //Update people list with people's ids
        people.removeAll(people);
        people.addAll(tempPeople);

        //Get outcome team
        String winningTeam = null;
        Margin margin = null;
        Result result = null;
        JSONObject jsonOutcome = (JSONObject) info.get("outcome");
        String winner;
        String jsonResult;
        try {
            winner = (String) jsonOutcome.get("winner");
            winningTeam = findTeam(winner, teamLists);
            result = Result.Win;
            if(keysContain(jsonOutcome.keySet(), "method")){
                if(jsonOutcome.get("method").equals("Awarded")){
                    result = Result.Awarded;
                }
            }
        }catch (Exception e){
            jsonResult = (String) jsonOutcome.get("result");
            if(jsonResult.equals("draw")){
                result = Result.Draw;
            }
            if(jsonResult.equals("tie")){
                result = Result.Tie;
            }
            if(jsonResult.equals("no result")){
                result = Result.No_Result;
            }
            if(result == null){
                result = Result.Draw;
            }
        }
        //Get margin
        int inningsMargin = 0;
        int winningMargin = 0;
        MarginType marginType = null;
        if(result.equals(Result.Win)){
            JSONObject by = (JSONObject) jsonOutcome.get("by");
            //by.keySet().forEach(System.out::println);
            if(by.keySet().contains("runs")){
                marginType = MarginType.RUNS;
                winningMargin = (int) by.get("runs");
            }
            if(by.keySet().contains("wickets")){
                marginType = MarginType.WICKETS;
                winningMargin = (int) by.get("wickets");
            }
            if(by.keySet().contains("innings")){
                inningsMargin = (int) by.get("innings");
            }
            margin = new Margin(marginType, winningMargin, inningsMargin);
        }
        if(result == Result.Win){
            outcome = new Outcome(result, winningTeam, margin);
        }else{
            outcome = new Outcome(result);
        }

        //Get Player of the match
        playerOfTheMatch = new ArrayList<>();
        try{
            JSONArray jsonPlayerOfMatch = (JSONArray) info.get("player_of_match");
            jsonPlayerOfMatch.forEach(p -> playerOfTheMatch.add(findPerson(p.toString(), people).getId().toString()));
        }catch (Exception e){
            System.out.printf("No PoM found");
        }

        //Get dates
        dates = new ArrayList<>();
        JSONArray jsonDates = (JSONArray) info.get("dates");
        jsonDates.forEach(d -> {
            dates.add(LocalDate.parse(d.toString()));
        });

        //Get toss information
        JSONObject jsonToss = (JSONObject) info.get("toss");
        tossWinner = findTeam(jsonToss.get("winner").toString(), teamLists);
        if(jsonToss.get("decision").toString().equals("bat")){
            tossDecision = Decision.Bat;
        }else{
            tossDecision = Decision.Field;
        }

        //Get General information
        ballsPerOver = (int) info.get("balls_per_over");
        try{
            city = (String) info.get("city");
        }catch (Exception e){
            city = "NULL";
            //noCityFiles.add(fileId);
        }
        matchNumber = 0;
        try{
            JSONObject jsonEvent = (JSONObject) info.get("event");
            matchNumber = (int) jsonEvent.get("match_number");
            event = (String) jsonEvent.get("name");
        }catch (Exception e){
            System.out.println("No event found");
        }

        if(info.get("gender").equals("male")){
            gender = Gender.Male;
        }else if(info.get("gender").equals("female")){
            gender = Gender.Female;
        }else{
            gender = Gender.Other;
        }
        if(info.get("match_type").equals("Test")){
            matchType = MatchType.Test;
        }else{
            matchType = MatchType.One_Day;
        }
        matchTypeNumber = (int) info.get("match_type_number");
        try{
            season = (String) info.get("season");
        }catch(Exception e){
            int seasonInt = (Integer) info.get("season");
            season = Integer.toString(seasonInt);
        }
        venue = (String) info.get("venue");

        //Get Innings
        innings = new ArrayList<>();
        JSONArray jsonInnings = (JSONArray) matchObject.get("innings");
        jsonInnings.forEach(i -> {
            System.out.println(fileId + ": Innings number: " + innings.size());
            JSONObject iCurrent = (JSONObject) i;
            String inTeam = findTeam(iCurrent.get("team").toString(), teamLists);
            boolean declared = false;
            if(keysContain(iCurrent.keySet(), "declared")){
                declared = (boolean) iCurrent.get("declared");
            }

            JSONArray jsonOvers = (JSONArray) iCurrent.get("overs");
            List<Over> overs = new ArrayList<>();
            jsonOvers.forEach(o -> {
                JSONObject oCurrent = (JSONObject) o;
                int overNum = (int) oCurrent.get("over");

                JSONArray jsonDeliveries = (JSONArray) oCurrent.get("deliveries");
                List<Delivery> deliveries = new ArrayList<>();
                jsonDeliveries.forEach(d -> {
                    JSONObject dCurrent = (JSONObject) d;
                    String batter = findPerson(dCurrent.get("batter").toString(), people).getId().toString();
                    String bowler = findPerson(dCurrent.get("bowler").toString(), people).getId().toString();
                    String nonStriker = findPerson(dCurrent.get("non_striker").toString(), people).getId().toString();
                    int byes = 0;
                    int legByes = 0;
                    int wides = 0;
                    int noBalls = 0;
                    int batterRuns = 0;
                    int extraRuns = 0;
                    int totalRuns = 0;
                    if(dCurrent.keySet().contains("extras")){
                        JSONObject jsonExtras = (JSONObject) dCurrent.get("extras");
                        for(Object k : jsonExtras.keySet()){
                            if(k.toString().equals("byes")){
                                byes += (int) jsonExtras.get("byes");
                            }
                            if(k.toString().equals("legbyes")){
                                legByes += (int) jsonExtras.get("legbyes");
                            }
                            if(k.toString().equals("wides")){
                                wides += (int) jsonExtras.get("wides");
                            }
                            if(k.toString().equals("noballs")){
                                noBalls += (int) jsonExtras.get("noballs");
                            }
                        }
                    }
                    JSONObject jsonRuns = (JSONObject) dCurrent.get("runs");
                    batterRuns = (int) jsonRuns.get("batter");
                    extraRuns = (int) jsonRuns.get("extras");
                    totalRuns = (int) jsonRuns.get("total");

                    //Get wickets
                    //Check if there is any wickets for current delivery
                    ArrayList<Wicket> wickets = new ArrayList<>();
                    if(deliveryContainsWickets(dCurrent.keySet())){
                        JSONArray jsonWickets = (JSONArray) dCurrent.get("wickets");
                        jsonWickets.forEach(w -> {
                            JSONObject wCurrent = (JSONObject) w;
                            String batterOut = findPerson(wCurrent.get("player_out").toString(), people).getId().toString();
                            Dismissal dismissalType = getDismissalType(wCurrent.get("kind").toString());
                            //Check if fielders involved
                            List<String> fielders = new ArrayList<>();
                            if(keysContain(wCurrent.keySet(), "fielders")){
                                JSONArray jsonFielders = (JSONArray) wCurrent.get("fielders");
                                jsonFielders.forEach(f -> {
                                    JSONObject fCurrent = (JSONObject) f;
                                    Person p = findPerson(fCurrent.get("name").toString(), people);
                                    //TODO Might be more appropriate ways to deal with the situation of substitute fielder being involved in a wicket but for now this should work
                                    if(p != null){
                                        fielders.add(p.getId().toString());
                                    }
                                });
                            }
                            //Check if caught and bowled add bowler to list of fielders
                            if(wCurrent.get("kind").equals("caught and bowled")){
                                fielders.add(bowler);
                            }
                            Wicket wicket;
                            if(fielders.size() < 1){
                                wicket = new Wicket(batterOut, dismissalType);
                            }else{
                                wicket = new Wicket(batterOut, fielders, dismissalType);
                            }
                            wickets.add(wicket);
                        });
                    }
                    Delivery delivery;
                    if(wickets.size() < 1){
                        delivery = new Delivery(batter, bowler, nonStriker, batterRuns, byes, legByes, wides, noBalls, extraRuns, totalRuns);
                    }else{
                        delivery = new Delivery(batter, bowler, nonStriker, batterRuns, byes, legByes, wides, noBalls, extraRuns, totalRuns, wickets);
                    }
                    deliveries.add(delivery);
                });
                Over over = new Over(overNum, deliveries);
                overs.add(over);
            });
            Innings newInnings = new Innings(inTeam, overs, declared);
            innings.add(newInnings);
        });

        UUID matchId = UUID.randomUUID();

        //Deal with series
        if(event != null && season != null){
            if(seriesRepository.existsByNameAndSeason(event, season)) {
                customSeriesRepository.addMatchesByNameAndSeason(event, season, matchId.toString());
                //seriesRepository.addMatchesByNameAndSeason(event, season, matchId.toString());
            }else{
                seriesRepository.insert(new Series(event, season));
                customSeriesRepository.addMatchesByNameAndSeason(event, season, matchId.toString());
                //seriesRepository.addMatchesByNameAndSeason(event, season, matchId.toString());
            }
        }

        TestMatch testMatch = new TestMatch(
                matchId.toString(),
                fileId,
                ballsPerOver,
                city,
                dates,
                event,
                matchNumber,
                gender,
                matchType,
                matchTypeNumber,
                matchReferees,
                tvUmpires,
                fieldUmpires,
                outcome,
                playerOfTheMatch,
                teamLists,
                season,
                tossWinner,
                tossDecision,
                venue,
                innings
        );
        matchRepository.insert(testMatch);
    }

    private Person findPerson(String name, List<Person> people){
        for(Person p:people){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }
    private Person findPersonByIdentifier(String id, List<Person> people){
        for(Person p : people){
            if(p.getIdentifier().equals(id)){
                return p;
            }
        }
        return null;
    }
    private String findTeam(String name, List<TeamList> teams){
        for(TeamList t : teams){
            if(t.getName().equals(name.toUpperCase())){
                return t.getId();
            }
        }
        return null;
    }
    private boolean deliveryContainsWickets(Set<String> keys){
        for(String k : keys){
            if(k.equals("wickets")){
                return true;
            }
        }
        return false;
    }
    private Dismissal getDismissalType(String s){
        return switch (s) {
            case "bowled" -> Dismissal.Bowled;
            case "caught", "caught and bowled" -> Dismissal.Caught;
            case "lbw" -> Dismissal.LBW;
            case "run out" -> Dismissal.Run_Out;
            case "stumped" -> Dismissal.Stumped;
            case "retired" -> Dismissal.Retired;
            case "hit ball twice" -> Dismissal.Hit_Ball_Twice;
            case "hit wicket" -> Dismissal.Hit_Wicket;
            case "obstructing field" -> Dismissal.Obstructing_Field;
            case "timed out" -> Dismissal.Timed_Out;
            default -> Dismissal.Bowled;
        };
    }
    private boolean keysContain(Set<String> keys, String string){
        for(String k : keys){
            if(k.equals(string)){
                return true;
            }
        }
        return false;
    }
}
