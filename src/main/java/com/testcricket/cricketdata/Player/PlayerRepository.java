package com.testcricket.cricketdata.Player;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {

    Player findPlayerByIdentifier(String identifier);

    List<Player> findPlayersByTeamsContaining(String team);

}
