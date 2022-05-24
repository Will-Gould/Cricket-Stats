package com.testcricket.cricketdata.Team;

import com.testcricket.cricketdata.Player.Player;
import com.testcricket.cricketdata.Player.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public List<Team> fetchAllTeams(){
        return teamRepository.findAll();
    }

    public Team fetchTeam(String id){
        return teamRepository.findTeamById(id);
    }

    public List<Player> fetchPlayersPlayedForTeam(String id){
        return playerRepository.findPlayersByTeamsContaining(id);
    }

}
