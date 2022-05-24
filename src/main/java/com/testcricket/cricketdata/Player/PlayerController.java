package com.testcricket.cricketdata.Player;

import com.testcricket.cricketdata.Profile.Role;
import com.testcricket.cricketdata.Util.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("api/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<Response> fetchAllPlayers(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("players", playerService.fetchAllPlayers()))
                        .message("Players retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(value = {"{playerId}", "{playerId}/{role}"})
    public ResponseEntity<Response> fetchPlayerProfile(@PathVariable String playerId, @PathVariable(required = false) String role){
        Role r;
        if(role == null){
            r = Role.BATTER;
        }else{
            switch(role){
                case "bowler" -> {r = Role.BOWLER;}
                default -> {r = Role.BATTER;}
            }
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("profile", playerService.fetchPlayerProfile(playerId, r)))
                        .message("Player retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
//        System.out.println(role);
//        if(role.equals("batter")){
//            return playerService.fetchPlayerProfile(playerId, Role.BATTER);
//        }
//        if(role.equals("bowler")){
//            return playerService.fetchPlayerProfile(playerId, Role.BOWLER);
//        }
//        return playerService.fetchPlayerProfile(playerId, Role.BATTER);
    }

    @GetMapping(value = {"{playerId}/{role}/{seriesId}"})
    public ResponseEntity<Response> fetchPlayerSeriesProfile(
            @PathVariable String playerId,
            @PathVariable(required = false) String role,
            @PathVariable String seriesId
    ){
        Role r;
        if(role == null){
            r = Role.BATTER;
        }else{
            switch(role){
                case "bowler" -> {r = Role.BOWLER;}
                default -> {r = Role.BATTER;}
            }
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("profile", playerService.fetchPlayerProfile(playerId, r, seriesId)))
                        .message("Player retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

}
