package com.testcricket.cricketdata.People;

import com.testcricket.cricketdata.Profile.PlayerProfile;
import com.testcricket.cricketdata.Profile.Role;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public List<Player> fetchAllPlayers(){
        return playerService.fetchAllPlayers();
    }

    @GetMapping("{playerId}/{role}")
    public Optional<PlayerProfile> fetchPlayerProfile(@PathVariable String playerId, @PathVariable String role){
        System.out.println(role);
        if(role.equals("batter")){
            return playerService.fetchPlayerProfile(playerId, Role.BATTER);
        }
        if(role.equals("bowler")){
            return playerService.fetchPlayerProfile(playerId, Role.BOWLER);
        }
        return playerService.fetchPlayerProfile(playerId, Role.BATTER);
    }

}
